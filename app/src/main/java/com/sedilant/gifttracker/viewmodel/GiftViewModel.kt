package com.sedilant.gifttracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sedilant.gifttracker.data.Gift
import com.sedilant.gifttracker.data.GiftRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel for managing gift tracking state and business logic
 */
class GiftViewModel(
    private val repository: GiftRepository = GiftRepository()
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filterPurchased = MutableStateFlow<Boolean?>(null)
    val filterPurchased: StateFlow<Boolean?> = _filterPurchased.asStateFlow()

    // Filtered and sorted gifts based on search and filter
    val gifts: StateFlow<List<Gift>> = combine(
        repository.gifts,
        _searchQuery,
        _filterPurchased
    ) { gifts, query, purchased ->
        gifts
            .filter { gift ->
                val matchesSearch = query.isEmpty() || 
                    gift.name.contains(query, ignoreCase = true) ||
                    gift.recipient.contains(query, ignoreCase = true)
                val matchesFilter = purchased == null || gift.isPurchased == purchased
                matchesSearch && matchesFilter
            }
            .sortedBy { it.recipient }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val totalSpent: StateFlow<Double> = repository.gifts
        .combine(_filterPurchased) { gifts, purchased ->
            gifts
                .filter { purchased == null || it.isPurchased == purchased }
                .sumOf { it.price }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    fun addGift(gift: Gift) {
        repository.addGift(gift)
    }

    fun updateGift(gift: Gift) {
        repository.updateGift(gift)
    }

    fun deleteGift(giftId: String) {
        repository.deleteGift(giftId)
    }

    fun togglePurchased(giftId: String) {
        repository.togglePurchased(giftId)
    }

    fun getGiftById(giftId: String): Gift? {
        return repository.getGiftById(giftId)
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFilter(purchased: Boolean?) {
        _filterPurchased.value = purchased
    }
}
