package com.sedilant.gifttracker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.sedilant.gifttracker.ui.giftDetail.GiftDetailScreen
import com.sedilant.gifttracker.ui.home.HomeScreen

// Define keys that will identify content
data object HomeScreen
data class GiftDetail(val id: String)
data object Summary

@Composable
fun GiftTrackerApp() {

    // If I have to implement it with Navigation Interfaces. I probably add hilt and with hilt someway
    // to be able to inject this back stack to the interface implementations. Then here I only need to
    // pass the interface to the composable.

    // Create a back stack, specifying the key the app should start with
    val backStack = remember { mutableStateListOf<Any>(HomeScreen) }
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<HomeScreen> {
                // HomeScreen composable
                HomeScreen(onOpenDetails = { id -> backStack.add(GiftDetail(id ?: "")) })
            }
            entry<GiftDetail>(
                metadata = mapOf("extraDataKey" to "extraDataValue")
            ) { key ->
                // GiftDetailScreen composable
                GiftDetailScreen(key.id)
            }
        }

    )
}