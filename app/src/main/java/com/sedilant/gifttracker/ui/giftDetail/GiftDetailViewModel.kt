package com.sedilant.gifttracker.ui.giftDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sedilant.gifttracker.data.local.entity.GiftEntity
import com.sedilant.gifttracker.data.repository.GiftRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Represents the UI state for the GiftDetailScreen.
 */
data class GiftDetailUiState(
    val person: String = "",
    val gift: String = "",
    val price: String = "",
    val isPurchased: Boolean = false,
    val isEditMode: Boolean = false,
    val giftId: Long? = null
)

@HiltViewModel(assistedFactory = GiftDetailViewModelFactory::class)
class GiftDetailViewModel @AssistedInject constructor(
    @Assisted private val itemId: Long?,
    private val giftRepository: GiftRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GiftDetailUiState())
    val uiState: StateFlow<GiftDetailUiState> = _uiState.asStateFlow()

    init {
        if (itemId == null) {
            // Creating a new item, start in edit mode
            _uiState.update { it.copy(isEditMode = true) }
        } else {
            // Loading an existing item, fetch details from database
            loadGiftDetails(itemId)
        }
    }

    private fun loadGiftDetails(giftId: Long?) {
        if (giftId == null) return

        viewModelScope.launch {
            val gift = giftRepository.getGiftById(giftId)
            gift?.let {
                _uiState.update { currentState ->
                    currentState.copy(
                        giftId = it.id,
                        person = it.person,
                        gift = it.name,
                        price = it.price.toString(),
                        isPurchased = it.isPurchased
                    )
                }
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
        viewModelScope.launch {
            val currentState = _uiState.value
            val priceValue = currentState.price.toFloatOrNull() ?: 0

            val giftEntity = GiftEntity(
                id = currentState.giftId ?: 0,
                name = currentState.gift,
                person = currentState.person,
                price = priceValue.toFloat(),
                isPurchased = currentState.isPurchased
            )

            if (currentState.giftId == null) {
                // Insert new gift
                val newId = giftRepository.insertGift(giftEntity)
                _uiState.update { it.copy(giftId = newId) }
            } else {
                // Update existing gift
                giftRepository.updateGift(giftEntity)
            }

            toggleEditMode()
        }
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
        // Allow numeric input with up to 2 decimal places
        if (newPrice.isEmpty() || newPrice.matches(Regex("^\\d+(\\.\\d{0,2})?$"))) {
            _uiState.update { it.copy(price = newPrice) }
        }
    }
}

@AssistedFactory
interface GiftDetailViewModelFactory {
    fun create(itemId: Long?): GiftDetailViewModel
}
