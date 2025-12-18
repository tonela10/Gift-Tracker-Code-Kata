package com.sedilant.gifttracker.ui.giftDetail

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sedilant.gifttracker.ui.theme.ChartGreen
import com.sedilant.gifttracker.ui.theme.RedPrimary

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnimatedButton(
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit,
    isEditMode: Boolean
) {
    val color by animateColorAsState(
        when {
            isLoading -> Color.Gray
            isEditMode -> ChartGreen
            else -> RedPrimary
        }
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val maxAvailableWidth = maxWidth
        val buttonHeight = 50.dp

        val width by animateDpAsState(
            targetValue = if (isLoading) buttonHeight else maxAvailableWidth,
            animationSpec = tween(durationMillis = 600),
            label = "Width Animation"
        )

        val cornerRadius by animateDpAsState(
            targetValue = if (isLoading) 25.dp else 8.dp,
            animationSpec = tween(durationMillis = 600),
            label = "Corner Animation"
        )

        Button(
            onClick = { if (!isLoading) onClick() },
            modifier = Modifier
                .height(buttonHeight)
                .width(width),
            shape = RoundedCornerShape(cornerRadius),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = color),
            enabled = !isLoading
        ) {
            Crossfade(targetState = isLoading, label = "Content Fade") { loading ->
                if (loading) {
                    LoadingIndicator(
                        modifier = Modifier.fillMaxSize(),
                        color = RedPrimary,
                    )
                } else {
                    Text(
                        text = text,
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Clip
                    )
                }
            }
        }
    }
}