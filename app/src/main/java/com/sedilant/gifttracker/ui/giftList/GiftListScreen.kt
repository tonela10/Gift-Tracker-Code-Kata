package com.sedilant.gifttracker.ui.giftList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sedilant.gifttracker.ui.theme.GiftTrackerTheme

@Composable
public fun GiftListScreen(
    onOpenDetails: (String?) -> Unit,
    viewModel: GiftListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(uiState.gifts) { gift ->
            GiftItem(
                onSeeDetails = { onOpenDetails(gift.name) },
                giftPrice = gift.price,
                giftName = gift.name,
                giftRecipient = gift.recipient,
                isPurchased = gift.isPurchased,
                onCheckItem = { viewModel.onGiftChecked(gift) },
            )
        }
    }
}

@Preview
@Composable
private fun GiftListScreenPreview() {
    GiftTrackerTheme {
        // We can't use a Hilt ViewModel in previews easily, so we'll simulate the state
        val gifts = listOf(
            Gift("Libro de Arte Moderno", "Ana", 25, false),
            Gift("Bufanda de lana", "Carlos", 30, true)
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
                )
            }
        }
    }
}

data class Gift(
    val name: String,
    val recipient: String,
    val price: Int,
    val isPurchased: Boolean
)
