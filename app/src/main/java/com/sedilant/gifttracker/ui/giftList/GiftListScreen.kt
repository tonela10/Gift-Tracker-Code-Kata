package com.sedilant.gifttracker.ui.giftList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
public fun GiftListScreen(
    onOpenDetails: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        Button(
            modifier = Modifier.align(Alignment.Center),
            onClick = onOpenDetails
        ) {
            Text("Click to navigate Details")
        }
    }
}