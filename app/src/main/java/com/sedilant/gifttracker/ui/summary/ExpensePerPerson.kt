package com.sedilant.gifttracker.ui.summary

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sedilant.gifttracker.data.local.entity.GiftEntity

@Composable
fun ExpensePerPerson(
    gifts: List<GiftEntity>,
    modifier: Modifier = Modifier
) {

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
