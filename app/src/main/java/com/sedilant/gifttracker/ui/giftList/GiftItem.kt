package com.sedilant.gifttracker.ui.giftList

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import com.sedilant.gifttracker.R
import com.sedilant.gifttracker.ui.theme.RedPrimary

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun GiftItem(
    modifier: Modifier = Modifier,
    giftPrice: Float,
    giftName: String,
    giftRecipient: String,
    isPurchased: Boolean,
    onSeeDetails: () -> Unit,
    onCheckItem: () -> Unit,
    onSwipeRight: () -> Unit,
) {
    var screenWidthPx by remember { mutableStateOf(1f) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .onSizeChanged { size ->
                screenWidthPx = size.width.toFloat()
            }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .matchParentSize(),
            color = RedPrimary,
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onSeeDetails)
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFDCD2D2)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                        )
                        .clickable(onClick = onCheckItem),
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

                Text(
                    text = "$giftPrice €",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
        }

        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(16.dp))
        ) {

        }
    }
}

@Preview
@Composable
private fun PreviewGiftItem() {
    GiftItem(
        giftPrice = 49.99f,
        giftName = "Smartwatch",
        giftRecipient = "Alex",
        isPurchased = true,
        onSeeDetails = {},
        onCheckItem = {},
        onSwipeRight = {}
    )
}