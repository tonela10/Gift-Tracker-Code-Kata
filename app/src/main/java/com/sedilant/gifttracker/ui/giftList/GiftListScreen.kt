package com.sedilant.gifttracker.ui.giftList

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

/**
 * viewModel::onGiftDelete(id:Long)
 * viewModel::onGiftChecked(gift:Gift)
 */
@Composable
public fun GiftListScreen(
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onOpenDetails: (Long?) -> Unit,
    viewModel: GiftListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedGiftId by remember { mutableStateOf<Long?>(null) }

}


@Preview
@Composable
private fun GiftListScreenPreview() {

}

data class Gift(
    val id: Long = 0,
    val name: String,
    val recipient: String,
    val price: Float,
    val isPurchased: Boolean
)
