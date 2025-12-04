package com.sedilant.gifttracker.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sedilant.gifttracker.ui.screens.GiftDetailScreenNew
import com.sedilant.gifttracker.ui.screens.GiftFormScreen
import com.sedilant.gifttracker.ui.screens.GiftListScreen
import com.sedilant.gifttracker.ui.state.GiftUiEvent
import com.sedilant.gifttracker.viewmodel.GiftDetailViewModel
import com.sedilant.gifttracker.viewmodel.GiftFormViewModel
import com.sedilant.gifttracker.viewmodel.GiftListViewModel
import kotlinx.coroutines.flow.collectLatest

/**
 * Navigation routes for the app
 */
object Routes {
    const val GIFT_LIST = "gift_list"
    const val ADD_GIFT = "add_gift"
    const val EDIT_GIFT = "edit_gift/{giftId}"
    const val GIFT_DETAIL = "gift_detail/{giftId}"
    
    fun editGift(giftId: Long) = "edit_gift/$giftId"
    fun giftDetail(giftId: Long) = "gift_detail/$giftId"
}

/**
 * Main navigation graph with Hilt ViewModels
 */
@Composable
fun GiftTrackerNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.GIFT_LIST,
        modifier = modifier,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it / 3 },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it / 3 },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        }
    ) {
        // Gift List Screen
        composable(Routes.GIFT_LIST) {
            val viewModel: GiftListViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            
            LaunchedEffect(Unit) {
                viewModel.uiEvent.collectLatest { event ->
                    when (event) {
                        is GiftUiEvent.NavigateToDetail -> {
                            navController.navigate(Routes.giftDetail(event.giftId))
                        }
                        is GiftUiEvent.NavigateToAddGift -> {
                            navController.navigate(Routes.ADD_GIFT)
                        }
                        else -> {}
                    }
                }
            }
            
            GiftListScreen(
                uiState = uiState,
                onSearchQueryChange = viewModel::onSearchQueryChange,
                onStatusFilterChange = viewModel::onStatusFilterChange,
                onClearFilters = viewModel::clearFilters,
                onGiftClick = viewModel::onGiftClick,
                onAdvanceStatus = viewModel::onAdvanceGiftStatus,
                onDeleteGift = viewModel::onDeleteGift,
                onAddGiftClick = viewModel::onAddGiftClick
            )
        }
        
        // Add Gift Screen
        composable(Routes.ADD_GIFT) {
            val viewModel: GiftFormViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            
            LaunchedEffect(Unit) {
                viewModel.uiEvent.collectLatest { event ->
                    when (event) {
                        is GiftUiEvent.NavigateBack -> navController.popBackStack()
                        else -> {}
                    }
                }
            }
            
            GiftFormScreen(
                uiState = uiState,
                onNameChange = viewModel::onNameChange,
                onRecipientChange = viewModel::onRecipientChange,
                onPriceChange = viewModel::onPriceChange,
                onStatusChange = viewModel::onStatusChange,
                onOccasionChange = viewModel::onOccasionChange,
                onNotesChange = viewModel::onNotesChange,
                onSave = viewModel::saveGift,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // Edit Gift Screen
        composable(
            route = Routes.EDIT_GIFT,
            arguments = listOf(navArgument("giftId") { type = NavType.LongType })
        ) {
            val viewModel: GiftFormViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            
            LaunchedEffect(Unit) {
                viewModel.uiEvent.collectLatest { event ->
                    when (event) {
                        is GiftUiEvent.NavigateBack -> navController.popBackStack()
                        else -> {}
                    }
                }
            }
            
            GiftFormScreen(
                uiState = uiState,
                onNameChange = viewModel::onNameChange,
                onRecipientChange = viewModel::onRecipientChange,
                onPriceChange = viewModel::onPriceChange,
                onStatusChange = viewModel::onStatusChange,
                onOccasionChange = viewModel::onOccasionChange,
                onNotesChange = viewModel::onNotesChange,
                onSave = viewModel::saveGift,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // Gift Detail Screen
        composable(
            route = Routes.GIFT_DETAIL,
            arguments = listOf(navArgument("giftId") { type = NavType.LongType })
        ) { backStackEntry ->
            val giftId = backStackEntry.arguments?.getLong("giftId") ?: 0L
            val viewModel: GiftDetailViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            
            LaunchedEffect(Unit) {
                viewModel.uiEvent.collectLatest { event ->
                    when (event) {
                        is GiftUiEvent.NavigateBack -> navController.popBackStack()
                        else -> {}
                    }
                }
            }
            
            GiftDetailScreenNew(
                uiState = uiState,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { navController.navigate(Routes.editGift(giftId)) },
                onAdvanceStatus = viewModel::onAdvanceStatus,
                onUpdateStatus = viewModel::onUpdateStatus,
                onShowDeleteConfirmation = viewModel::onShowDeleteConfirmation,
                onDismissDeleteConfirmation = viewModel::onDismissDeleteConfirmation,
                onConfirmDelete = viewModel::onDeleteGift
            )
        }
    }
}
