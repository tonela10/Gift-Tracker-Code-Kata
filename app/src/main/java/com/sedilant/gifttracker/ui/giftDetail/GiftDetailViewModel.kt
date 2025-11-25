package com.sedilant.gifttracker.ui.giftDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Represents the UI state for the GiftDetailScreen.
 */
data class GiftDetailUiState(
    val person: String = "",
    val gift: String = "",
    val price: String = "",
    val isPurchased: Boolean = false,
    val isEditMode: Boolean = false
)

@HiltViewModel
class GiftDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val itemId: String? = savedStateHandle["itemId"]

    private val _uiState = MutableStateFlow(GiftDetailUiState())
    val uiState: StateFlow<GiftDetailUiState> = _uiState.asStateFlow()

    init {
        if (itemId == null) {
            // Creating a new item, start in edit mode
            _uiState.update { it.copy(isEditMode = true) }
        } else {
            // Loading an existing item, fetch details
            // For now, using mock data as in the image
            _uiState.update {
                it.copy(
                    person = "Carlos",
                    gift = "Bufanda de lana",
                    price = "30",
                    isPurchased = true
                )
            }
        }
    }

    /**
     * Toggles the edit mode on or off.
     */
    fun toggleEditMode() {
        _uiState.update { currentState ->
            currentState.copy(isEditMode = !currentState.isEditMode)
        }
    }

    /**
     * Saves the gift details and exits edit mode.
     */
    fun saveGiftDetails() {
        // Here you would implement the logic to save the data
        // to your repository or database.
        toggleEditMode()
    }
}
