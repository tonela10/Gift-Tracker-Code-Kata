package com.sedilant.gifttracker.ui.giftDetail

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

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