package com.sedilant.gifttracker.ui.giftList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sedilant.gifttracker.ui.theme.GiftTrackerTheme

@Composable
public fun GiftListScreen(
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onOpenDetails: (Long?) -> Unit,
    viewModel: GiftListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedGiftId by remember { mutableStateOf<Long?>(null) }
    val paddingValues = PaddingValues(
        top = 12.dp,
        bottom = contentPadding.calculateBottomPadding() + 12.dp,
        start = contentPadding.calculateStartPadding(layoutDirection = LocalLayoutDirection.current) + 12.dp,
        end = contentPadding.calculateEndPadding(LocalLayoutDirection.current) + 12.dp,
    )

    if (selectedGiftId != null) {
        AlertDialog(
            onDismissRequest = { selectedGiftId = null },
            title = { Text(text = "WARNING") },
            text = { Text("Are you sure you want to delete this gift?") },
            confirmButton = {
                androidx.compose.material3.Button(onClick = {
                    selectedGiftId?.let { viewModel.onGiftDelete(it) }
                    selectedGiftId = null
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                androidx.compose.material3.Button(onClick = { selectedGiftId = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = paddingValues
    ) {
        items(uiState.gifts) { gift ->
            GiftItem(
                onSeeDetails = { onOpenDetails(gift.id) },
                giftPrice = gift.price,
                giftName = gift.name,
                giftRecipient = gift.recipient,
                isPurchased = gift.isPurchased,
                onCheckItem = { viewModel.onGiftChecked(gift) },
                onLongClick = { selectedGiftId = gift.id },
            )
        }
    }
}


@Preview
@Composable
private fun GiftListScreenPreview() {
    GiftTrackerTheme {
        val gifts = listOf(
            Gift(0, "Libro de Arte Moderno", "Ana", 25f, false),
            Gift(1, "Bufanda de lana", "Carlos", 30f, true)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(gifts) { gift ->
                GiftItem(
                    onSeeDetails = { },
                    giftPrice = gift.price,
                    giftName = gift.name,
                    giftRecipient = gift.recipient,
                    isPurchased = gift.isPurchased,
                    onCheckItem = { },
                    onLongClick = {}
                )
            }
        }
    }
}

data class Gift(
    val id: Long = 0,
    val name: String,
    val recipient: String,
    val price: Float,
    val isPurchased: Boolean
)
