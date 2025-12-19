package com.sedilant.gifttracker.ui.summary


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
public fun SummaryScreen(
    viewModel: SummaryViewModel = hiltViewModel(),
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val uiState by viewModel.uiState.collectAsState()
    SummaryScreenStateless(
        uiState = uiState,
        onGiftClick = viewModel::updateGift,
        contentPadding = contentPadding
    )
}

@Composable
public fun SummaryScreenStateless(
    uiState: SummaryUiState,
    onGiftClick: (Long) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {

}

@Preview(widthDp = 1000)
@Preview
@Composable
private fun PreviewSummaryScreen() {
    SummaryScreenStateless(
        uiState = SummaryUiState(),
        onGiftClick = {}
    )
}
