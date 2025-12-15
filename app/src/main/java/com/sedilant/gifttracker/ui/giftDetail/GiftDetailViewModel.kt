package com.sedilant.gifttracker.ui.giftDetail

import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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

@HiltViewModel(assistedFactory = GiftDetailViewModelFactory::class)
class GiftDetailViewModel @AssistedInject constructor(
    @Assisted private val itemId: String?,
) : ViewModel() {

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

    /**
     * Updates the person's name in the UI state.
     */
    fun onPersonChange(newPerson: String) {
        _uiState.update { it.copy(person = newPerson) }
    }

    /**
     * Updates the gift's name in the UI state.
     */
    fun onGiftChange(newGift: String) {
        _uiState.update { it.copy(gift = newGift) }
    }

    /**
     * Updates the price in the UI state.
     */
    fun onPriceChange(newPrice: String) {
        // You could add input validation here if needed
        _uiState.update { it.copy(price = newPrice) }
    }
}

@AssistedFactory
interface GiftDetailViewModelFactory {
    fun create(itemId: String?): GiftDetailViewModel
}
