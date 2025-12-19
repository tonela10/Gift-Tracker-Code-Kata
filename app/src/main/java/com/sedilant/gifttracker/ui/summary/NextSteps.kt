package com.sedilant.gifttracker.ui.summary

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.sedilant.gifttracker.data.local.entity.GiftEntity

@Composable
internal fun NextSteps(
) {

}

@Composable
private fun NextStepsRow(

) {

}

@Preview
@Composable
private fun NextStepsPreview() {
    val pendingGiftsList = listOf(
        GiftEntity(1, "Cámara de fotos", "Ana", 150.00f, false),
        GiftEntity(2, "Libro de recetas", "Luis", 30.00f, false),
        GiftEntity(3, "Auriculares inalámbricos", "Marta", 80.00f, false)
    )
}


@Preview(showBackground = true)
@Composable
private fun NextStepsRowPreview() {
}
