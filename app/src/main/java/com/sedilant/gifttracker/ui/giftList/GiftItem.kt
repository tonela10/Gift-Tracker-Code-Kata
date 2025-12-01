package com.sedilant.gifttracker.ui.giftList

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sedilant.gifttracker.R
import com.sedilant.gifttracker.ui.theme.ChartYellow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GiftItem(
    modifier: Modifier = Modifier,
    giftPrice: Int,
    giftName: String,
    giftRecipient: String,
    isPurchased: Boolean,
    onSeeDetails: () -> Unit,
    onCheckItem: () -> Unit,
) {
    // first box animation
    val redBoxSlideProgress = remember { Animatable(0f) }
    val redBoxAlpha = remember { Animatable(1f) }

    // first ribbon animation
    val verticalBoxSlideProgress = remember { Animatable(0f) }
    val verticalBoxAlpha = remember { Animatable(0f) }

    // second ribbon animation
    val horizontalBoxSlideProgress = remember { Animatable(0f) }
    val horizontalBoxAlpha = remember { Animatable(0f) }

    // Animation sequence
    LaunchedEffect(isPurchased) {
        if (isPurchased) {
            // Reset animations
            redBoxSlideProgress.snapTo(0f)
            redBoxAlpha.snapTo(1f)
            verticalBoxSlideProgress.snapTo(0f)
            verticalBoxAlpha.snapTo(1f)
            horizontalBoxSlideProgress.snapTo(0f)
            horizontalBoxAlpha.snapTo(1f)

            // Slide in from left (600ms)
            redBoxSlideProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 200, easing = EaseIn)
            )
            verticalBoxSlideProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 200, easing = EaseIn)
            )
            horizontalBoxSlideProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 200, easing = EaseIn)
            )

            delay(500)

            // Fade out (500ms)
            launch {
                redBoxAlpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 500)
                )
            }
            launch {
                verticalBoxAlpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 500)
                )
            }
            launch {
                horizontalBoxAlpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 500)
                )
            }

        } else {
            // If unchecked, immediately hide
            redBoxSlideProgress.snapTo(0f)
            redBoxAlpha.snapTo(0f)
            verticalBoxAlpha.snapTo(0f)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSeeDetails() },
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
            // Animated overlay using Animatable - constrain all overlays to card size
        }
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(16.dp))
        ) {
            // Red box
            if (redBoxSlideProgress.value > 0f || redBoxAlpha.value > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            translationX = -size.width * (1f - redBoxSlideProgress.value)
                            this.alpha = redBoxAlpha.value
                        }
                        .background(Color.Red.copy(alpha = 0.9f))
                )
            }


            // Diagonal ribbon (rotated vertical ribbon) 1
            if (verticalBoxSlideProgress.value > 0f || verticalBoxAlpha.value > 0f) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter) // Change alignment to TopStart
                        .width(16.dp)
                        .fillMaxHeight()
                        .graphicsLayer {
                            translationX = size.width
                            translationY = -15f
                            scaleY = verticalBoxSlideProgress.value * 1.5f
                            transformOrigin = TransformOrigin(0.5f, 0f) // Scale from top
                            rotationZ = -40f
                            this.alpha = verticalBoxAlpha.value
                        }
                        .background(ChartYellow)
                        .border(
                            width = 1.dp,
                            color = Color.Black
                        )
                )
            }
            // Horizontal white ribbon
            if (horizontalBoxSlideProgress.value > 0f || horizontalBoxAlpha.value > 0f) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .height(16.dp)
                        .fillMaxWidth()
                        .graphicsLayer {
                            translationX = -size.width * (1f - horizontalBoxSlideProgress.value)
                            this.alpha = horizontalBoxAlpha.value
                        }
                        .background(ChartYellow)
                        .border(
                            width = 1.dp,
                            color = Color.Black
                        )
                )
            }
        }
    }
}
