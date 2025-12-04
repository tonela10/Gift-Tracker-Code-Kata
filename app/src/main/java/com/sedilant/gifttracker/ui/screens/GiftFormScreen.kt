package com.sedilant.gifttracker.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sedilant.gifttracker.data.local.GiftStatus
import com.sedilant.gifttracker.data.local.Occasion
import com.sedilant.gifttracker.domain.model.Gift
import com.sedilant.gifttracker.ui.state.GiftFormUiState
import com.sedilant.gifttracker.ui.theme.GiftTrackerTheme

/**
 * Screen for adding or editing a gift
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiftFormScreen(
    uiState: GiftFormUiState,
    onNameChange: (String) -> Unit,
    onRecipientChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onStatusChange: (GiftStatus) -> Unit,
    onOccasionChange: (Occasion) -> Unit,
    onNotesChange: (String) -> Unit,
    onSave: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var priceText by remember(uiState.gift.price) {
        mutableStateOf(if (uiState.gift.price > 0) uiState.gift.price.toString() else "")
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (uiState.isEditing) "Edit Gift" else "Add Gift")
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onSave,
                icon = {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Icon(Icons.Default.Save, contentDescription = null)
                    }
                },
                text = { Text(if (uiState.isEditing) "Update" else "Save") },
                containerColor = MaterialTheme.colorScheme.primary
            )
        },
        modifier = modifier
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Error banner
            AnimatedVisibility(
                visible = uiState.error != null,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Error,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = uiState.error ?: "",
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
            
            // Gift Name
            OutlinedTextField(
                value = uiState.gift.name,
                onValueChange = onNameChange,
                label = { Text("Gift Name *") },
                placeholder = { Text("e.g., Smart Watch") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = uiState.validationErrors.nameError != null,
                supportingText = uiState.validationErrors.nameError?.let {
                    { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                leadingIcon = {
                    Icon(Icons.Default.CardGiftcard, contentDescription = null)
                },
                shape = RoundedCornerShape(12.dp)
            )
            
            // Recipient
            OutlinedTextField(
                value = uiState.gift.recipient,
                onValueChange = onRecipientChange,
                label = { Text("Recipient *") },
                placeholder = { Text("e.g., John") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = uiState.validationErrors.recipientError != null,
                supportingText = uiState.validationErrors.recipientError?.let {
                    { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null)
                },
                shape = RoundedCornerShape(12.dp)
            )
            
            // Price
            OutlinedTextField(
                value = priceText,
                onValueChange = { newValue ->
                    val filtered = newValue.filter { it.isDigit() || it == '.' }
                    // Prevent multiple decimal points
                    if (filtered.count { it == '.' } <= 1) {
                        priceText = filtered
                        onPriceChange(filtered)
                    }
                },
                label = { Text("Price *") },
                placeholder = { Text("0.00") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                prefix = { Text("$") },
                isError = uiState.validationErrors.priceError != null,
                supportingText = uiState.validationErrors.priceError?.let {
                    { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                leadingIcon = {
                    Icon(Icons.Default.AttachMoney, contentDescription = null)
                },
                shape = RoundedCornerShape(12.dp)
            )
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            
            // Status selector
            Text(
                text = "Status",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            
            StatusSelector(
                selectedStatus = uiState.gift.status,
                onStatusSelected = onStatusChange
            )
            
            // Occasion selector
            Text(
                text = "Occasion",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            
            OccasionSelector(
                selectedOccasion = uiState.gift.occasion,
                onOccasionSelected = onOccasionChange
            )
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            
            // Notes
            OutlinedTextField(
                value = uiState.gift.notes,
                onValueChange = onNotesChange,
                label = { Text("Notes (optional)") },
                placeholder = { Text("Add any additional details...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5,
                leadingIcon = {
                    Icon(Icons.Default.Notes, contentDescription = null)
                },
                shape = RoundedCornerShape(12.dp)
            )
            
            Text(
                text = "* Required fields",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            // Spacer for FAB
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StatusSelector(
    selectedStatus: GiftStatus,
    onStatusSelected: (GiftStatus) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        GiftStatus.entries.forEach { status ->
            FilterChip(
                selected = selectedStatus == status,
                onClick = { onStatusSelected(status) },
                label = { Text(status.displayName) },
                leadingIcon = if (selectedStatus == status) {
                    { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                } else null
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun OccasionSelector(
    selectedOccasion: Occasion,
    onOccasionSelected: (Occasion) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Occasion.entries.forEach { occasion ->
            FilterChip(
                selected = selectedOccasion == occasion,
                onClick = { onOccasionSelected(occasion) },
                label = { Text(occasion.displayName) },
                leadingIcon = if (selectedOccasion == occasion) {
                    { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                } else null
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GiftFormScreenPreview() {
    GiftTrackerTheme {
        GiftFormScreen(
            uiState = GiftFormUiState(
                gift = Gift(
                    name = "Smart Watch",
                    recipient = "John",
                    price = 299.99,
                    status = GiftStatus.IDEA,
                    occasion = Occasion.BIRTHDAY
                )
            ),
            onNameChange = {},
            onRecipientChange = {},
            onPriceChange = {},
            onStatusChange = {},
            onOccasionChange = {},
            onNotesChange = {},
            onSave = {},
            onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GiftFormScreenEmptyPreview() {
    GiftTrackerTheme {
        GiftFormScreen(
            uiState = GiftFormUiState(),
            onNameChange = {},
            onRecipientChange = {},
            onPriceChange = {},
            onStatusChange = {},
            onOccasionChange = {},
            onNotesChange = {},
            onSave = {},
            onNavigateBack = {}
        )
    }
}
