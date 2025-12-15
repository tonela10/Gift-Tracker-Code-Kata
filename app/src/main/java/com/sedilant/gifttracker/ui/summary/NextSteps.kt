package com.sedilant.gifttracker.ui.summary

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sedilant.gifttracker.R
import com.sedilant.gifttracker.data.local.entity.GiftEntity

@Composable
internal fun NextSteps(
    modifier: Modifier = Modifier,
    pendingGiftsList: List<GiftEntity>,
    onGiftClicked: () -> Unit
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onSecondary)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.next_steps_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            pendingGiftsList.forEach { gift ->
                NextStepsRow(
                    leadingIcon = null,
                    giftName = gift.name,
                    person = gift.person,
                    price = gift.price.toString(),
                    onGiftClicked = { onGiftClicked() }
                )
            }
        }
    }
}

@Composable
private fun NextStepsRow(
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    giftName: String,
    person: String,
    price: String,
    onGiftClicked: () -> Unit,
) {
    // Colors close to the mock: soft icon background + neutral tints
    val iconBg = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f)
    val iconTint = MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onGiftClicked)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            if (leadingIcon != null) {
                leadingIcon()
            } else {
                Icon(
                    imageVector = Icons.Outlined.Spa,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.size(12.dp))

        Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center) {
            Text(
                text = giftName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Para: $person",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(R.string.euro_symbol) + price,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Preview
@Composable
private fun NextStepsPreview() {
    NextSteps(
        pendingGiftsList = listOf(
            GiftEntity(1, "Cámara de fotos", "Ana", 150.00f, false),
            GiftEntity(2, "Libro de recetas", "Luis", 30.00f, false),
            GiftEntity(3, "Auriculares inalámbricos", "Marta", 80.00f, false)
        ),
        onGiftClicked = {}
    )
}


@Preview(showBackground = true)
@Composable
private fun NextStepsRowPreview() {
    NextStepsRow(
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Event,
                contentDescription = null,
                tint = Color(0xFFB23A48),
                modifier = Modifier.size(32.dp)
            )
        },
        giftName = "Cámara de fotos",
        person = "Ana",
        price = "150.00",
        onGiftClicked = {}
    )
}
