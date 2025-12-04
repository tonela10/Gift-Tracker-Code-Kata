package com.sedilant.gifttracker.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Repository for managing gift data
 * In a real app, this would interact with a database or network API
 */
class GiftRepository {
    private val _gifts = MutableStateFlow<List<Gift>>(emptyList())
    val gifts: StateFlow<List<Gift>> = _gifts.asStateFlow()

    init {
        // Initialize with sample data
        _gifts.value = listOf(
            Gift(
                name = "Smart Watch",
                recipient = "John",
                price = 299.99,
                isPurchased = false,
                notes = "Black color preferred"
            ),
            Gift(
                name = "Book Set",
                recipient = "Sarah",
                price = 49.99,
                isPurchased = true,
                notes = "Fantasy series"
            ),
            Gift(
                name = "Coffee Maker",
                recipient = "Mom",
                price = 89.99,
                isPurchased = false,
                notes = "With milk frother"
            )
        )
    }

    fun addGift(gift: Gift) {
        _gifts.value = _gifts.value + gift
    }

    fun updateGift(gift: Gift) {
        _gifts.value = _gifts.value.map { 
            if (it.id == gift.id) gift else it 
        }
    }

    fun deleteGift(giftId: String) {
        _gifts.value = _gifts.value.filter { it.id != giftId }
    }

    fun getGiftById(giftId: String): Gift? {
        return _gifts.value.find { it.id == giftId }
    }

    fun togglePurchased(giftId: String) {
        _gifts.value = _gifts.value.map { gift ->
            if (gift.id == giftId) {
                gift.copy(isPurchased = !gift.isPurchased)
            } else {
                gift
            }
        }
    }
}
