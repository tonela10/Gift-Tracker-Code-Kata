package com.sedilant.gifttracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sedilant.gifttracker.data.local.GiftStatus
import com.sedilant.gifttracker.data.local.Occasion
import com.sedilant.gifttracker.domain.model.Gift
import com.sedilant.gifttracker.domain.repository.GiftRepository
import com.sedilant.gifttracker.ui.state.GiftListUiState
import com.sedilant.gifttracker.ui.state.GiftStats
import com.sedilant.gifttracker.ui.state.GiftUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel for the Gift List screen
 * Follows unidirectional data flow pattern
 */
@HiltViewModel
class GiftListViewModel @Inject constructor(
    private val repository: GiftRepository
) : ViewModel() {
    
    private val _searchQuery = MutableStateFlow("")
    private val _statusFilter = MutableStateFlow<GiftStatus?>(null)
    private val _occasionFilter = MutableStateFlow<Occasion?>(null)
    private val _sortByRecipient = MutableStateFlow(false)
    
    private val _uiEvent = Channel<GiftUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
    
    val uiState: StateFlow<GiftListUiState> = combine(
        repository.getAllGifts(),
        repository.getTotalBudget(),
        repository.getTotalSpent(),
        _searchQuery,
        _statusFilter,
        _occasionFilter,
        _sortByRecipient
    ) { values ->
        @Suppress("UNCHECKED_CAST")
        val gifts = values[0] as List<Gift>
        val totalBudget = values[1] as Double
        val totalSpent = values[2] as Double
        val searchQuery = values[3] as String
        val statusFilter = values[4] as GiftStatus?
        val occasionFilter = values[5] as Occasion?
        val sortByRecipient = values[6] as Boolean
        
        GiftListUiState(
            gifts = gifts,
            isLoading = false,
            searchQuery = searchQuery,
            statusFilter = statusFilter,
            occasionFilter = occasionFilter,
            sortByRecipient = sortByRecipient,
            totalBudget = totalBudget,
            totalSpent = totalSpent,
            giftStats = GiftStats(
                totalGifts = gifts.size,
                ideasCount = gifts.count { it.status == GiftStatus.IDEA },
                purchasedCount = gifts.count { it.status == GiftStatus.PURCHASED },
                wrappedCount = gifts.count { it.status == GiftStatus.WRAPPED },
                givenCount = gifts.count { it.status == GiftStatus.GIVEN }
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GiftListUiState()
    )
    
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }
    
    fun onStatusFilterChange(status: GiftStatus?) {
        _statusFilter.value = status
    }
    
    fun onOccasionFilterChange(occasion: Occasion?) {
        _occasionFilter.value = occasion
    }
    
    fun onSortByRecipientChange(sortByRecipient: Boolean) {
        _sortByRecipient.value = sortByRecipient
    }
    
    fun clearFilters() {
        _searchQuery.value = ""
        _statusFilter.value = null
        _occasionFilter.value = null
    }
    
    fun onGiftClick(giftId: Long) {
        viewModelScope.launch {
            _uiEvent.send(GiftUiEvent.NavigateToDetail(giftId))
        }
    }
    
    fun onAddGiftClick() {
        viewModelScope.launch {
            _uiEvent.send(GiftUiEvent.NavigateToAddGift)
        }
    }
    
    fun onUpdateGiftStatus(giftId: Long, status: GiftStatus) {
        viewModelScope.launch {
            repository.updateGiftStatus(giftId, status).fold(
                onSuccess = {
                    _uiEvent.send(GiftUiEvent.ShowSnackbar("Status updated"))
                },
                onFailure = { e ->
                    Timber.e(e, "Failed to update gift status")
                    _uiEvent.send(GiftUiEvent.ShowSnackbar("Failed to update status"))
                }
            )
        }
    }
    
    fun onAdvanceGiftStatus(gift: Gift) {
        gift.nextStatus()?.let { nextStatus ->
            onUpdateGiftStatus(gift.id, nextStatus)
        }
    }
    
    fun onDeleteGift(giftId: Long) {
        viewModelScope.launch {
            repository.deleteGift(giftId).fold(
                onSuccess = {
                    _uiEvent.send(GiftUiEvent.ShowSnackbar("Gift deleted"))
                },
                onFailure = { e ->
                    Timber.e(e, "Failed to delete gift")
                    _uiEvent.send(GiftUiEvent.ShowSnackbar("Failed to delete gift"))
                }
            )
        }
    }
}
