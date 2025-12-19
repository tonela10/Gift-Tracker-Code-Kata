package com.sedilant.gifttracker.ui.giftDetail

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AnimatedButton(
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit,
    isEditMode: Boolean
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val maxAvailableWidth = maxWidth
        val buttonHeight = 50.dp

        Button(
            onClick = { if (!isLoading) onClick() },
            modifier = Modifier
                .height(buttonHeight)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(0.dp),
            enabled = !isLoading
        ) {
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
