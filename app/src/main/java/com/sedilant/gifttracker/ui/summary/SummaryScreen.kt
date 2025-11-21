package com.sedilant.gifttracker.ui.summary


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
public fun SummaryScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Responsive FlowRow that shows all cards in one row on tablets
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            maxItemsInEachRow = 3, // Allow up to 3 cards per row
        ) {
            SummaryInfoCard(
                modifier = Modifier
                    .weight(1f)
                    .widthIn(min = 150.dp), // Minimum width for compact screens
                icon = Icons.Default.ShoppingCart,
                title = "Regalos totales",
                backgroundColor = Color(0xFFFFE4E1),
                contentColor = Color(0xFFD32F2F),
                content = {
                    Text(
                        text = "12",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "para 5 personas",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            )

            SummaryInfoCard(
                modifier = Modifier
                    .weight(1f)
                    .widthIn(min = 150.dp),
                icon = Icons.Default.CheckCircle,
                title = "Comprados",
                backgroundColor = Color(0xFFE8F5E9),
                contentColor = Color(0xFF2E7D32),
                content = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "8 / 12",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                            Text(
                                text = "67%",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                        // Progress bar
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { 0.67f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = Color(0xFF2E7D32),
                            trackColor = Color(0xFFC8E6C9)
                        )
                    }
                }
            )

            SummaryInfoCard(
                modifier = Modifier
                    .weight(1f)
                    .widthIn(min = 150.dp),
                icon = Icons.Default.CheckCircle,
                title = "Gasto total estimado",
                backgroundColor = Color(0xFFFFF9C4),
                contentColor = Color(0xFFF9A825),
                content = {
                    Text(
                        text = "€358",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            )
        }
    }
}

@Composable
private fun SummaryInfoCard(
    icon: ImageVector,
    title: String,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Black,
    contentColor: Color = Color.Gray,
) {
    Surface(
        modifier = modifier
            .heightIn(min = 140.dp, max = 180.dp),
        color = backgroundColor,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = contentColor
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Custom content passed by the developer
            content()
        }
    }
}

@Preview
@Composable
private fun PreviewSummaryScreen() {
    SummaryScreen()
}

@Preview
@Composable
private fun PreviewSummaryInfoCard() {
    SummaryInfoCard(
        icon = Icons.Default.CheckCircle,
        title = "Comprados",
        content = {
            Text(
                text = "8 / 12",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
        }
    )
}