package com.sedilant.gifttracker

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.sedilant.gifttracker.ui.giftDetail.GiftDetailScreen
import com.sedilant.gifttracker.ui.home.HomeScreen
import kotlinx.serialization.Serializable

// Define keys that will identify content
@Serializable
data object HomeScreen : NavKey

@Serializable
data class GiftDetail(val id: String?) : NavKey

@Composable
fun GiftTrackerApp() {

    // If I have to implement it with Navigation Interfaces. I probably add hilt and with hilt someway
    // to be able to inject this back stack to the interface implementations. Then here I only need to
    // pass the interface to the composable.

    // Create a back stack, specifying the key the app should start with
    val backStack = rememberNavBackStack(HomeScreen)
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<HomeScreen> {
                // HomeScreen composable
                HomeScreen(onOpenDetails = { id -> backStack.add(GiftDetail(id)) })
            }
            entry<GiftDetail>(
                metadata = mapOf("extraDataKey" to "extraDataValue")
            ) { key ->
                // GiftDetailScreen composable
                GiftDetailScreen(
                    key.id,
                    onNavigateBack = { backStack.removeLastOrNull() }
                )
            }
        }

    )
}