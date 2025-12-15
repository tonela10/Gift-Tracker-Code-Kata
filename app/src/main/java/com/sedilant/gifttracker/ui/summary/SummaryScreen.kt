package com.sedilant.gifttracker.ui.summary


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
public fun SummaryScreen(
    viewModel: SummaryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(span = { GridItemSpan(if (maxLineSpan == 1) 1 else if (maxLineSpan == 2) 1 else 1) }) {
                SummaryInfoCard(
                    icon = Icons.Default.ShoppingCart,
                    title = "Regalos totales",
                    backgroundColor = Color(0xFFFFE4E1),
                    contentColor = Color(0xFFD32F2F),
                    content = {
                        Text(
                            text = "${uiState.totalGifts}",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "para ${uiState.totalPeople} personas",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                )
            }
            item(span = { GridItemSpan(if (maxLineSpan == 1) 1 else if (maxLineSpan == 2) 1 else 1) }) {
                SummaryInfoCard(
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
                                    text = "${uiState.purchasedGifts} / ${uiState.totalGifts}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "${(uiState.purchasedPercentage * 100).toInt()}%",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { uiState.purchasedPercentage },
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
            }
            item(span = { GridItemSpan(if (maxLineSpan == 1 || maxLineSpan == 2) maxLineSpan else 1) }) {
                SummaryInfoCard(
                    icon = Icons.Default.CheckCircle,
                    title = "Gasto total estimado",
                    backgroundColor = Color(0xFFFFF9C4),
                    contentColor = Color(0xFFF9A825),
                    content = {
                        Text(
                            text = "€${uiState.totalEstimatedCost}",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                )
            }
        }
    }
}


@Preview(widthDp = 1000)
@Preview
@Composable
private fun PreviewSummaryScreen() {
    SummaryScreen()
}
