package com.sedilant.gifttracker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.sedilant.gifttracker.ui.giftDetail.GiftDetailScreen
import com.sedilant.gifttracker.ui.giftList.GiftListScreen
import com.sedilant.gifttracker.ui.summary.SummaryScreen

// Define keys that will identify content
data object GiftList
data class GiftDetail(val id: String)
data object Summary

@Composable
fun GiftTrackerApp() {

    // Create a back stack, specifying the key the app should start with
    val backStack = remember { mutableStateListOf<Any>(GiftList) }
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<GiftList> {
                // GiftListComposable composable
                GiftListScreen(onOpenDetails = { backStack.add(GiftDetail("")) })
            }
            entry<GiftDetail>(
                metadata = mapOf("extraDataKey" to "extraDataValue")
            ) { key ->
                // GiftDetailScreen composable
                GiftDetailScreen(key.id)
            }
            entry<Summary> {
                // SummaryScreen composable
                SummaryScreen()
            }
        }

    )
}