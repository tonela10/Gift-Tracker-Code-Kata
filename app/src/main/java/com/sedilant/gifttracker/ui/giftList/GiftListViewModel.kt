package com.sedilant.gifttracker.ui.giftList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GiftListUiState(
    val gifts: List<Gift> = emptyList()
)

@HiltViewModel
class GiftListViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(GiftListUiState())
    val uiState: StateFlow<GiftListUiState> = _uiState.asStateFlow()

    init {
        // TODO: Replace with your data source (e.g., a repository)
        val initialGifts = listOf(
            Gift("Libro de Arte Moderno", "Ana", 25, false),
            Gift("Bufanda de lana", "Carlos", 30, true),
            Gift("Auriculares Inalámbricos", "Mamá", 79, false),
            Gift("Set de jardinería", "Abuela", 45, true)
        )
        _uiState.value = GiftListUiState(gifts = initialGifts)
    }

    fun onGiftChecked(gift: Gift) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val updatedGifts = currentState.gifts.map {
                    if (it.name == gift.name) {
                        it.copy(isPurchased = !it.isPurchased)
                    } else {
                        it
                    }
                }
                currentState.copy(gifts = updatedGifts)
            }
        }
    }
}
