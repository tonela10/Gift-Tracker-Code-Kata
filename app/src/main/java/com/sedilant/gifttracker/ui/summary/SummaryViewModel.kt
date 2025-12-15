package com.sedilant.gifttracker.ui.summary

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

data class SummaryUiState(
    val totalGifts: Int = 0,
    val purchasedGifts: Int = 0,
    val totalPeople: Int = 0,
    val totalEstimatedCost: Int = 0,
    val purchasedPercentage: Float = 0f
)

@HiltViewModel
class SummaryViewModel @Inject constructor(
    private val giftRepository: GiftRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SummaryUiState())
    val uiState: StateFlow<SummaryUiState> = _uiState.asStateFlow()

    init {
        loadSummaryData()
    }

    private fun loadSummaryData() {
        viewModelScope.launch {
            giftRepository.getAllGifts().collect { giftEntities ->
                val totalGifts = giftEntities.size
                val purchasedGifts = giftEntities.count { it.isPurchased }
                val totalPeople = giftEntities.map { it.person }.distinct().size
                val totalEstimatedCost = giftEntities.sumOf { it.price }
                val purchasedPercentage = if (totalGifts > 0) {
                    purchasedGifts.toFloat() / totalGifts.toFloat()
                } else {
                    0f
                }

                _uiState.update {
                    it.copy(
                        totalGifts = totalGifts,
                        purchasedGifts = purchasedGifts,
                        totalPeople = totalPeople,
                        totalEstimatedCost = totalEstimatedCost,
                        purchasedPercentage = purchasedPercentage
                    )
                }
            }
        }
    }
}
