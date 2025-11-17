package com.sedilant.gifttracker.ui.giftList

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sedilant.gifttracker.R
import com.sedilant.gifttracker.ui.theme.GiftTrackerTheme


@Composable
public fun GiftListScreen(
    onOpenDetails: (String?) -> Unit
) {

    // TODO move this list to a room or dataStore to persist it and be able to modify
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
                onClick = { onOpenDetails(gift.name) },
                giftPrice = gift.price,
                giftName = gift.name,
                giftRecipient = gift.recipient,
                isPurchased = gift.isPurchased
            )
        }
    }
}

@Composable
fun GiftItem(
    giftPrice: Int,
    giftName: String,
    giftRecipient: String,
    isPurchased: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox circle
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        color = if (isPurchased) Color(0xFF4A7C59) else Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = if (isPurchased) Color(0xFF4A7C59) else Color(0xFFCCCCCC),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isPurchased) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.item_purchased),
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Gift info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = giftName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Text(
                    text = "Para: $giftRecipient",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            // Price
            Text(
                text = "$giftPrice €",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = Color.Black
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