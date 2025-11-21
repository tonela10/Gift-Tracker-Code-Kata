package com.sedilant.gifttracker.ui.giftList

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sedilant.gifttracker.R
import com.sedilant.gifttracker.ui.theme.GiftTrackerTheme
import com.sedilant.gifttracker.ui.theme.RedPrimary
import kotlinx.coroutines.delay


@Composable
public fun GiftListScreen(
    onOpenDetails: (String?) -> Unit
) {
    // TODO create a viewModel to handle the ui
    val gifts = remember {
        listOf(
            Gift("Libro de Arte Moderno", "Ana", 25, false),
            Gift("Bufanda de lana", "Carlos", 30, true),
            Gift("Auriculares Inalámbricos", "Mamá", 79, false),
            Gift("Set de jardinería", "Abuela", 45, true)
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(gifts) { gift ->
            GiftItem(
                onSeeDetails = { onOpenDetails(gift.name) },
                giftPrice = gift.price,
                giftName = gift.name,
                giftRecipient = gift.recipient,
                isPurchased = gift.isPurchased,
                onCheckItem = {},
            )
        }
    }
}

@Preview
@Composable
private fun GiftListScreenPreview() {
    GiftTrackerTheme {
        GiftListScreen(
            onOpenDetails = {}
        )
    }
}

data class Gift(
    val name: String,
    val recipient: String,
    val price: Int,
    val isPurchased: Boolean
)