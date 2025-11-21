package com.sedilant.gifttracker.ui.giftList

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay


// TODO remove before merge
@Preview
@Composable
private fun Test(
    modifier: Modifier = Modifier,
    giftPrice: Int = 1,
    giftName: String = "Auriculares",
    giftRecipient: String = "Mamá",
    onClick: () -> Unit = {}
) {
    var isCheck by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    // Coroutine to run the timed animation sequence
    LaunchedEffect(expanded) {
        // This effect only runs when 'expanded' is set to true
        if (expanded) {
            // Wait for slide-in (2000ms)
            delay(2000)

            // Hold visible (1000ms)
            delay(1000)

            // Trigger fade-out
            expanded = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    // 1. Toggle the persistent state
                    isCheck = !isCheck
                    onClick()

                    // 2. ONLY start the animation sequence if checking the card
                    if (isCheck) {
                        expanded = true
                    } else {
                        // If unchecking, immediately stop the overlay
                        expanded = false
                    }
                },
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
                // ... (Check box and gift info content remains the same)
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            color = if (isCheck) Color(0xFF4A7C59) else Color.White,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .border(
                            width = 2.dp,
                            color = if (isCheck) Color(0xFF4A7C59) else Color(0xFFCCCCCC),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCheck) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Item purchased",
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

        // Animated overlay
        AnimatedVisibility(
            visible = expanded, // Driven by the animation sequence
            enter = slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing)
            ),
            exit = fadeOut(
                animationSpec = tween(durationMillis = 500)
            )
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Red.copy(alpha = 0.7f))
            )
        }
    }
}