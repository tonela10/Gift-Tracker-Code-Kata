package com.sedilant.gifttracker.ui.giftList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sedilant.gifttracker.data.repository.GiftRepository
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
class GiftListViewModel @Inject constructor(
    private val giftRepository: GiftRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GiftListUiState())
    val uiState: StateFlow<GiftListUiState> = _uiState.asStateFlow()

    init {
        loadGifts()
    }

    private fun loadGifts() {
        viewModelScope.launch {
            giftRepository.getAllGifts().collect { giftEntities ->
                val gifts = giftEntities.map { entity ->
                    Gift(
                        id = entity.id,
                        name = entity.name,
                        recipient = entity.person,
                        price = entity.price,
                        isPurchased = entity.isPurchased
                    )
                }
                _uiState.update { it.copy(gifts = gifts) }
            }
        }
    }

    fun onGiftChecked(gift: Gift) {
        viewModelScope.launch {
            giftRepository.updatePurchasedStatus(gift.id, !gift.isPurchased)
        }
    }
}
