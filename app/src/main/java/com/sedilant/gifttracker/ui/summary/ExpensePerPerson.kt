package com.sedilant.gifttracker.ui.summary

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sedilant.gifttracker.data.local.entity.GiftEntity
import kotlin.random.Random

@Composable
fun ExpensePerPerson(
    gifts: List<GiftEntity>,
    modifier: Modifier = Modifier
) {
    val expensePerPerson: List<Pair<String, Double>> = gifts
        .groupBy { it.person }
        .map { (person, personGifts) ->
            person to personGifts.sumOf { it.price.toDouble() }
        }
        .sortedByDescending { it.second }

    val totalExpense = expensePerPerson.sumOf { it.second }


    fun randomColor(): Color {
        val rnd = Random.Default
        val hue = rnd.nextFloat() * 360f
        val saturation = 0.35f
        val value = 0.98f
        val hsv = floatArrayOf(hue, saturation, value)
        val argb = android.graphics.Color.HSVToColor(hsv)
        return Color(argb)
    }

    // Generate a color per slice using randomColor()
    val sliceColors: List<Color> = expensePerPerson.map { randomColor() }

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onSecondary)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Gasto por persona",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Donut Chart
                Canvas(
                    modifier = Modifier
                        .size(150.dp)
                        .padding(8.dp)
                ) {
                    val radius = size.minDimension / 2
                    val center = Offset(size.width / 2, size.height / 2)
                    var startAngle = -90f

                    expensePerPerson.forEachIndexed { index, (_, amount) ->
                        val sweepAngle =
                            if (totalExpense > 0.0) ((amount / totalExpense) * 360f).toFloat() else 0f
                        drawArc(
                            color = sliceColors[index],
                            startAngle = startAngle,
                            sweepAngle = sweepAngle,
                            useCenter = false,
                            topLeft = Offset(center.x - radius, center.y - radius),
                            size = Size(radius * 2, radius * 2),
                            style = Stroke(width = 24.dp.toPx())
                        )
                        startAngle += sweepAngle
                    }

                    // Center text
                    drawContext.canvas.nativeCanvas.apply {
                        val paint = android.graphics.Paint().apply {
                            color = android.graphics.Color.BLACK
                            textSize = 36.dp.toPx()
                            typeface = android.graphics.Typeface.create(
                                android.graphics.Typeface.DEFAULT,
                                android.graphics.Typeface.BOLD
                            )
                            textAlign = android.graphics.Paint.Align.CENTER
                        }
                        drawText(
                            "€${totalExpense.toInt()}",
                            center.x,
                            center.y + (paint.textSize / 3),
                            paint
                        )
                    }
                }

                // Legend
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    expensePerPerson.forEachIndexed { index, (name, amount) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(
                                        color = sliceColors[index],
                                        shape = CircleShape
                                    )
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = name,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Text(
                                text = "€${amount.toInt()}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = sliceColors[index]
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ExpensePerPersonPreview() {
    val sampleGifts = listOf(
        GiftEntity(1, "Ana", "Perfume", 50.0f, false),
        GiftEntity(2, "Luis", "Libro", 20.0f, false),
        GiftEntity(3, "Ana", "Bufanda", 30.0f, false),
        GiftEntity(4, "Carlos", "Reloj", 80.0f, false),
        GiftEntity(5, "Luis", "Camiseta", 25.0f, false),
        GiftEntity(6, "Marta", "Bolso", 60.0f, false)
    )

    ExpensePerPerson(gifts = sampleGifts)
}
