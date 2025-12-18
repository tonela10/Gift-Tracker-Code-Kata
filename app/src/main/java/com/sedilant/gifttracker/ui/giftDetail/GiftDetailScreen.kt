package com.sedilant.gifttracker.ui.giftDetail

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Euro
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sedilant.gifttracker.R
import com.sedilant.gifttracker.ui.theme.BackgroundLight

@Composable
fun GiftDetailScreen(
    itemId: Long?,
    viewModel: GiftDetailViewModel = hiltViewModel(
        creationCallback = { factory: GiftDetailViewModelFactory ->
            factory.create(itemId = itemId)
        }),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    GiftDetailScreenStateless(
        uiState = uiState,
        onToggleEditMode = viewModel::toggleEditMode,
        onSaveGiftDetails = viewModel::saveGiftDetails,
        onNavigateBack = onNavigateBack,
        onPersonChange = viewModel::onPersonChange,
        onGiftChange = viewModel::onGiftChange,
        onPriceChange = viewModel::onPriceChange
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiftDetailScreenStateless(
    uiState: GiftDetailUiState,
    onToggleEditMode: () -> Unit,
    onSaveGiftDetails: () -> Unit,
    onNavigateBack: () -> Unit,
    onPersonChange: (String) -> Unit,
    onGiftChange: (String) -> Unit,
    onPriceChange: (String) -> Unit
) {
    Scaffold(
        topBar = {
            DetailsTopBar(
                isEditMode = uiState.isEditMode,
                onToggleEditMode = onToggleEditMode,
                onNavigateBack = onNavigateBack
            )
        },
        bottomBar = {
            DetailsBottomBar(
                isEditMode = uiState.isEditMode,
                isLoading = uiState.isLoading,
                onToggleEditMode = onToggleEditMode,
                onSaveGiftDetails = {
                    onSaveGiftDetails()
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(BackgroundLight)
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DetailItemCard(
                label = stringResource(id = R.string.person_label),
                value = uiState.person,
                isEditMode = uiState.isEditMode,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
                onValueChange = { onPersonChange(it) }
            )
            DetailItemCard(
                label = stringResource(id = R.string.gift_label),
                value = uiState.gift,
                isEditMode = uiState.isEditMode,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
                onValueChange = { onGiftChange(it) }
            )
            DetailItemCard(
                label = stringResource(id = R.string.price_label),
                value = uiState.price,
                isEditMode = uiState.isEditMode,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                onValueChange = { onPriceChange(it) },
                trailingIcon = { Icon(imageVector = Icons.Rounded.Euro, contentDescription = null) }
            )
            StatusItemCard(isPurchased = uiState.isPurchased)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsTopBar(
    isEditMode: Boolean,
    onToggleEditMode: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Surface(modifier = Modifier.background(BackgroundLight)) {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = if (!isEditMode) stringResource(id = R.string.gift_detail_title) else stringResource(
                            id = R.string.editing
                        ),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            navigationIcon = {
                Crossfade(
                    targetState = isEditMode,
                    label = "nav-icon-animation"
                ) { isEditMode ->
                    if (isEditMode) {
                        IconButton(onClick = { onToggleEditMode() }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(id = R.string.exit_edit_mode_content_description)
                            )
                        }
                    } else {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = stringResource(id = R.string.back_content_description)
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun DetailsBottomBar(
    isEditMode: Boolean,
    isLoading: Boolean,
    onToggleEditMode: () -> Unit,
    onSaveGiftDetails: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        val buttonText =
            if (isEditMode) stringResource(id = R.string.save_button) else stringResource(id = R.string.edit_button)
        val onClickAction = if (isEditMode) {
            { onSaveGiftDetails() }
        } else {
            { onToggleEditMode() }
        }

        AnimatedButton(
            text = buttonText,
            isLoading = isLoading,
            onClick = onClickAction,
            isEditMode = isEditMode
        )
    }
}

@Preview
@Composable
private fun GiftDetailScreenPreview() {
    GiftDetailScreenStateless(
        uiState = GiftDetailUiState(),
        onToggleEditMode = {},
        onSaveGiftDetails = {},
        onNavigateBack = {},
        onPersonChange = {},
        onGiftChange = {},
        onPriceChange = {}
    )
}