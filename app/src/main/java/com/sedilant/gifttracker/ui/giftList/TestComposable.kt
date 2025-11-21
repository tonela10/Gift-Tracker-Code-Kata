package com.sedilant.gifttracker.ui.giftList

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sedilant.gifttracker.ui.theme.ChartYellow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    // first box animation
    val redBoxSlideProgress = remember { Animatable(0f) }
    val redBoxAlpha = remember { Animatable(1f) }

    // first cinta animation
    val verticalBoxSlideProgress = remember { Animatable(0f) }
    val verticalBoxAlpha = remember { Animatable(0f) }

    // second vertical ribbon animation
    val verticalBoxSlideProgress2 = remember { Animatable(0f) }
    val verticalBoxAlpha2 = remember { Animatable(0f) }

    // third vertical ribbon animation
    val verticalBoxSlideProgress3 = remember { Animatable(0f) }
    val verticalBoxAlpha3 = remember { Animatable(0f) }


    // second cinta animation
    val horizontalBoxSlideProgress = remember { Animatable(0f) }
    val horizontalBoxAlpha = remember { Animatable(0f) }

    // ribbon icon animation
    val ribbonAlpha = remember { Animatable(0f) }
    val ribbonScale = remember { Animatable(0f) }

    // Animation sequence
    LaunchedEffect(isCheck) {
        if (isCheck) {
            // Reset animations
            redBoxSlideProgress.snapTo(0f)
            redBoxAlpha.snapTo(1f)
            verticalBoxSlideProgress.snapTo(0f)
            verticalBoxAlpha.snapTo(1f)
            verticalBoxSlideProgress2.snapTo(0f)
            verticalBoxAlpha2.snapTo(1f)
            verticalBoxSlideProgress3.snapTo(0f)
            verticalBoxAlpha3.snapTo(1f)
            horizontalBoxSlideProgress.snapTo(0f)
            horizontalBoxAlpha.snapTo(1f)
            ribbonAlpha.snapTo(0f)
            ribbonScale.snapTo(0f)

            // Slide in from left (600ms)
            redBoxSlideProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 200, easing = EaseIn)
            )
            verticalBoxSlideProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 200, easing = EaseIn)
            )
            verticalBoxSlideProgress2.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 200, easing = EaseIn)
            )
            verticalBoxSlideProgress3.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 200, easing = EaseIn)
            )
            horizontalBoxSlideProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 200, easing = EaseIn)
            )

            // Show ribbon icon with scale animation
            launch {
                ribbonAlpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 300)
                )
            }
            ribbonScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 300, easing = EaseIn)
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
                verticalBoxAlpha2.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 500)
                )
            }
            launch {
                verticalBoxAlpha3.animateTo(
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
            launch {
                ribbonAlpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 500)
                )
            }

        } else {
            // If unchecked, immediately hide
            redBoxSlideProgress.snapTo(0f)
            redBoxAlpha.snapTo(0f)
            verticalBoxAlpha.snapTo(0f)
            horizontalBoxAlpha.snapTo(0f)
            ribbonAlpha.snapTo(0f)
            ribbonScale.snapTo(0f)
        }
    }

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    isCheck = !isCheck
                    onClick()
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

        // Animated overlay using Animatable - constrain all overlays to card size
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
            // Diagonal ribbon (rotated vertical ribbon) 2
            if (verticalBoxSlideProgress2.value > 0f || verticalBoxAlpha2.value > 0f) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter) // Change alignment to TopStart
                        .width(16.dp)
                        .fillMaxHeight()
                        .graphicsLayer {
                            translationX = size.width * 3f
                            translationY = -15f
                            scaleY = verticalBoxSlideProgress2.value * 1.5f
                            transformOrigin = TransformOrigin(0.5f, 0f) // Scale from top
                            rotationZ = -40f
                            this.alpha = verticalBoxAlpha2.value
                        }
                        .background(ChartYellow)
                        .border(
                            width = 1.dp,
                            color = Color.Black
                        )
                )
            }
            // Diagonal ribbon (rotated vertical ribbon) 3
            if (verticalBoxSlideProgress3.value > 0f || verticalBoxAlpha3.value > 0f) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter) // Change alignment to TopStart
                        .width(16.dp)
                        .fillMaxHeight()
                        .graphicsLayer {
                            translationX = size.width * 5f
                            translationY = -15f
                            scaleY = verticalBoxSlideProgress3.value * 1.5f
                            transformOrigin = TransformOrigin(0.5f, 0f) // Scale from top
                            rotationZ = -40f
                            this.alpha = verticalBoxAlpha3.value
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
//
//            // Center ribbon icon
//            if (ribbonAlpha.value > 0f) {
//                Icon(
//                    painter = painterResource(id = R.drawable.ribbon),
//                    contentDescription = "Gift ribbon",
//                    modifier = Modifier
//                        .align(Alignment.Center)
//                        .size(48.dp)
//                        .graphicsLayer {
//                            scaleX = ribbonScale.value
//                            scaleY = ribbonScale.value
//                            alpha = ribbonAlpha.value
//                        },
//                    tint = ChartYellow
//                )
//            }
        }
    }
}

@Preview
@Composable
private fun TestPreview() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Test { }
    }
}

