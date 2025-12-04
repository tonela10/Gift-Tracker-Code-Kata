package com.sedilant.gifttracker.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sedilant.gifttracker.data.local.GiftStatus
import com.sedilant.gifttracker.data.local.Occasion
import com.sedilant.gifttracker.domain.model.Gift
import com.sedilant.gifttracker.ui.state.GiftListUiState
import com.sedilant.gifttracker.ui.state.GiftStats
import com.sedilant.gifttracker.ui.theme.GiftTrackerTheme
import java.text.NumberFormat
import java.util.*

/**
 * Main gift list screen with filtering, search, and statistics
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiftListScreen(
    uiState: GiftListUiState,
    onSearchQueryChange: (String) -> Unit,
    onStatusFilterChange: (GiftStatus?) -> Unit,
    onClearFilters: () -> Unit,
    onGiftClick: (Long) -> Unit,
    onAdvanceStatus: (Gift) -> Unit,
    onDeleteGift: (Long) -> Unit,
    onAddGiftClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showFilterSheet by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text(
                            text = "🎁 Gift Tracker",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (uiState.giftStats.totalGifts > 0) {
                            Text(
                                text = "${uiState.giftStats.totalGifts} gifts • ${formatCurrency(uiState.totalBudget)} total",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    if (uiState.hasFilters) {
                        IconButton(onClick = onClearFilters) {
                            Icon(
                                Icons.Default.FilterListOff,
                                contentDescription = "Clear filters",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    IconButton(onClick = { showFilterSheet = true }) {
                        BadgedBox(
                            badge = {
                                if (uiState.hasFilters) {
                                    Badge { Text("!") }
                                }
                            }
                        ) {
                            Icon(Icons.Default.FilterList, contentDescription = "Filter")
                        }
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddGiftClick,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Add Gift") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        },
        modifier = modifier
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search bar
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            
            // Status filter chips
            StatusFilterChips(
                selectedStatus = uiState.statusFilter,
                onStatusSelected = onStatusFilterChange,
                stats = uiState.giftStats,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            // Content
            when {
                uiState.isLoading -> {
                    LoadingState(modifier = Modifier.fillMaxSize())
                }
                uiState.isEmpty -> {
                    EmptyState(
                        onAddGiftClick = onAddGiftClick,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                uiState.filteredGifts.isEmpty() -> {
                    NoResultsState(
                        onClearFilters = onClearFilters,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                else -> {
                    GiftList(
                        gifts = uiState.filteredGifts,
                        onGiftClick = onGiftClick,
                        onAdvanceStatus = onAdvanceStatus,
                        onDeleteGift = onDeleteGift,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
    
    if (showFilterSheet) {
        FilterBottomSheet(
            currentStatus = uiState.statusFilter,
            onStatusSelected = {
                onStatusFilterChange(it)
                showFilterSheet = false
            },
            onDismiss = { showFilterSheet = false }
        )
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = { Text("Search gifts or recipients...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            AnimatedVisibility(
                visible = query.isNotEmpty(),
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Close, contentDescription = "Clear")
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(28.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    )
}

@Composable
private fun StatusFilterChips(
    selectedStatus: GiftStatus?,
    onStatusSelected: (GiftStatus?) -> Unit,
    stats: GiftStats,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedStatus == null,
                onClick = { onStatusSelected(null) },
                label = { Text("All (${stats.totalGifts})") },
                leadingIcon = if (selectedStatus == null) {
                    { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                } else null
            )
        }
        
        items(GiftStatus.entries) { status ->
            val count = when (status) {
                GiftStatus.IDEA -> stats.ideasCount
                GiftStatus.PURCHASED -> stats.purchasedCount
                GiftStatus.WRAPPED -> stats.wrappedCount
                GiftStatus.GIVEN -> stats.givenCount
            }
            
            FilterChip(
                selected = selectedStatus == status,
                onClick = { onStatusSelected(if (selectedStatus == status) null else status) },
                label = { Text("${status.emoji} $count") },
                leadingIcon = if (selectedStatus == status) {
                    { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                } else null
            )
        }
    }
}

@Composable
private fun GiftList(
    gifts: List<Gift>,
    onGiftClick: (Long) -> Unit,
    onAdvanceStatus: (Gift) -> Unit,
    onDeleteGift: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = gifts,
            key = { it.id }
        ) { gift ->
            GiftCard(
                gift = gift,
                onClick = { onGiftClick(gift.id) },
                onAdvanceStatus = { onAdvanceStatus(gift) },
                modifier = Modifier.animateItem(
                    fadeInSpec = tween(durationMillis = 250),
                    fadeOutSpec = tween(durationMillis = 250),
                    placementSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            )
        }
        
        // Bottom spacing for FAB
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun GiftCard(
    gift: Gift,
    onClick: () -> Unit,
    onAdvanceStatus: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Status indicator
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        when (gift.status) {
                            GiftStatus.IDEA -> MaterialTheme.colorScheme.surfaceVariant
                            GiftStatus.PURCHASED -> MaterialTheme.colorScheme.primaryContainer
                            GiftStatus.WRAPPED -> MaterialTheme.colorScheme.secondaryContainer
                            GiftStatus.GIVEN -> MaterialTheme.colorScheme.tertiaryContainer
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = gift.status.emoji,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Gift info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = gift.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Outlined.Person,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = gift.recipient,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Text(
                        text = "•",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Text(
                        text = gift.occasion.displayName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // Price and action
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = formatCurrency(gift.price),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                if (gift.nextStatus() != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    FilledTonalIconButton(
                        onClick = onAdvanceStatus,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = "Advance status",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyState(
    onAddGiftClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🎁",
            style = MaterialTheme.typography.displayLarge
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "No gifts yet",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Start tracking your gifts by adding your first one!",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(onClick = onAddGiftClick) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Your First Gift")
        }
    }
}

@Composable
private fun NoResultsState(
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Outlined.SearchOff,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "No results found",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Try adjusting your search or filters",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        TextButton(onClick = onClearFilters) {
            Text("Clear Filters")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterBottomSheet(
    currentStatus: GiftStatus?,
    onStatusSelected: (GiftStatus?) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "Filter by Status",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // All option
            FilterOption(
                text = "All Gifts",
                emoji = "📋",
                selected = currentStatus == null,
                onClick = { onStatusSelected(null) }
            )
            
            GiftStatus.entries.forEach { status ->
                FilterOption(
                    text = status.displayName,
                    emoji = status.emoji,
                    selected = currentStatus == status,
                    onClick = { onStatusSelected(status) }
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun FilterOption(
    text: String,
    emoji: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .background(
                if (selected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = emoji, style = MaterialTheme.typography.titleLarge)
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        
        if (selected) {
            Icon(
                Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

private fun formatCurrency(amount: Double): String {
    return NumberFormat.getCurrencyInstance(Locale.US).format(amount)
}

@Preview(showBackground = true)
@Composable
private fun GiftListScreenPreview() {
    GiftTrackerTheme {
        GiftListScreen(
            uiState = GiftListUiState(
                gifts = listOf(
                    Gift(
                        id = 1,
                        name = "Smart Watch",
                        recipient = "John",
                        price = 299.99,
                        status = GiftStatus.IDEA,
                        occasion = Occasion.BIRTHDAY
                    ),
                    Gift(
                        id = 2,
                        name = "Book Set",
                        recipient = "Sarah",
                        price = 49.99,
                        status = GiftStatus.PURCHASED,
                        occasion = Occasion.CHRISTMAS
                    )
                ),
                isLoading = false,
                giftStats = GiftStats(
                    totalGifts = 2,
                    ideasCount = 1,
                    purchasedCount = 1
                ),
                totalBudget = 349.98
            ),
            onSearchQueryChange = {},
            onStatusFilterChange = {},
            onClearFilters = {},
            onGiftClick = {},
            onAdvanceStatus = {},
            onDeleteGift = {},
            onAddGiftClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyStatePreview() {
    GiftTrackerTheme {
        EmptyState(onAddGiftClick = {})
    }
}
