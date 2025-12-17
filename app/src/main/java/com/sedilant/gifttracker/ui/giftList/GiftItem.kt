package com.sedilant.gifttracker.ui.giftList

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import com.sedilant.gifttracker.R
import com.sedilant.gifttracker.ui.theme.ChartYellow
import com.sedilant.gifttracker.ui.theme.RedPrimary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

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
    val swipeableState = rememberSwipeableState(0)
    val anchors = mapOf(0f to 0, screenWidthPx to 1)

    LaunchedEffect(swipeableState.currentValue) {
        if (swipeableState.currentValue == 1) {
            onSwipeRight()
            swipeableState.animateTo(
                targetValue = 0,
                anim = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
            )
        }
    }

    // RIBBON ANIMATION BLOCK
    val redBoxSlideProgress = remember { Animatable(0f) }
    val redBoxAlpha = remember { Animatable(1f) }
    val verticalBoxSlideProgress = remember { Animatable(0f) }
    val verticalBoxAlpha = remember { Animatable(0f) }
    val verticalBoxSlideProgress2 = remember { Animatable(0f) }
    val verticalBoxAlpha2 = remember { Animatable(0f) }
    val verticalBoxSlideProgress3 = remember { Animatable(0f) }
    val verticalBoxAlpha3 = remember { Animatable(0f) }
    val horizontalBoxSlideProgress = remember { Animatable(0f) }
    val horizontalBoxAlpha = remember { Animatable(0f) }
    val ribbonAlpha = remember { Animatable(0f) }
    val ribbonScale = remember { Animatable(0f) }

    var isFirstComposition by remember { mutableStateOf(true) }

    LaunchedEffect(isPurchased) {
        if (isFirstComposition) {
            isFirstComposition = false
            if (!isPurchased) {
                redBoxSlideProgress.snapTo(0f)
                redBoxAlpha.snapTo(0f)
                verticalBoxAlpha.snapTo(0f)
            }
            return@LaunchedEffect
        }

        if (isPurchased) {
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

            ribbonAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 300)
            )

            ribbonScale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )

            delay(1000)

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
            redBoxSlideProgress.snapTo(0f)
            redBoxAlpha.snapTo(0f)
            verticalBoxAlpha.snapTo(0f)
            horizontalBoxAlpha.snapTo(0f)
            ribbonAlpha.snapTo(0f)
            ribbonScale.snapTo(0f)
        }
    }
    // END OF THE RIBBON ANIMATION BLOCK

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
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    orientation = Orientation.Horizontal,
                    enabled = true,
                    reverseDirection = false,
                    thresholds = { _, _ -> FractionalThreshold(0.5f) },
                )
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
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

            if (verticalBoxSlideProgress.value > 0f || verticalBoxAlpha.value > 0f) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .width(16.dp)
                        .fillMaxHeight()
                        .graphicsLayer {
                            translationX = size.width
                            translationY = -15f
                            scaleY = verticalBoxSlideProgress.value * 1.5f
                            transformOrigin = TransformOrigin(0.5f, 0f)
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

            if (verticalBoxSlideProgress2.value > 0f || verticalBoxAlpha2.value > 0f) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .width(16.dp)
                        .fillMaxHeight()
                        .graphicsLayer {
                            translationX = size.width * 3f
                            translationY = -15f
                            scaleY = verticalBoxSlideProgress2.value * 1.5f
                            transformOrigin = TransformOrigin(0.5f, 0f)
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

            if (verticalBoxSlideProgress3.value > 0f || verticalBoxAlpha3.value > 0f) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .width(16.dp)
                        .fillMaxHeight()
                        .graphicsLayer {
                            translationX = size.width * 5f
                            translationY = -15f
                            scaleY = verticalBoxSlideProgress3.value * 1.5f
                            transformOrigin = TransformOrigin(0.5f, 0f)
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

            if (ribbonAlpha.value > 0f) {
                Image(
                    painter = painterResource(id = R.drawable.ribbon_removebg_preview),
                    contentDescription = "Gift ribbon",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(100.dp)
                        .graphicsLayer {
                            translationX = size.width
                            scaleX = ribbonScale.value
                            scaleY = ribbonScale.value
                            alpha = ribbonAlpha.value
                        },
                )
            }
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