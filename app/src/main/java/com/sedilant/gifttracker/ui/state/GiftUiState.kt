package com.sedilant.gifttracker.ui.state

import com.sedilant.gifttracker.data.local.GiftStatus
import com.sedilant.gifttracker.data.local.Occasion
import com.sedilant.gifttracker.domain.model.Gift

/**
 * UI State for the Gift List screen
 */
data class GiftListUiState(
    val gifts: List<Gift> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val searchQuery: String = "",
    val statusFilter: GiftStatus? = null,
    val occasionFilter: Occasion? = null,
    val sortByRecipient: Boolean = false,
    val totalBudget: Double = 0.0,
    val totalSpent: Double = 0.0,
    val giftStats: GiftStats = GiftStats()
) {
    val filteredGifts: List<Gift>
        get() = gifts.filter { gift ->
            val matchesSearch = searchQuery.isEmpty() ||
                gift.name.contains(searchQuery, ignoreCase = true) ||
                gift.recipient.contains(searchQuery, ignoreCase = true)
            val matchesStatus = statusFilter == null || gift.status == statusFilter
            val matchesOccasion = occasionFilter == null || gift.occasion == occasionFilter
            matchesSearch && matchesStatus && matchesOccasion
        }.let { filtered ->
            if (sortByRecipient) filtered.sortedBy { it.recipient }
            else filtered.sortedByDescending { it.createdAt }
        }
    
    val isEmpty: Boolean
        get() = !isLoading && gifts.isEmpty()
    
    val hasFilters: Boolean
        get() = searchQuery.isNotEmpty() || statusFilter != null || occasionFilter != null
}

/**
 * Statistics for gifts
 */
data class GiftStats(
    val totalGifts: Int = 0,
    val ideasCount: Int = 0,
    val purchasedCount: Int = 0,
    val wrappedCount: Int = 0,
    val givenCount: Int = 0
) {
    val completionPercentage: Float
        get() = if (totalGifts > 0) givenCount.toFloat() / totalGifts else 0f
}

/**
 * UI State for Add/Edit Gift screen
 */
data class GiftFormUiState(
    val gift: Gift = Gift.EMPTY,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isEditing: Boolean = false,
    val error: String? = null,
    val validationErrors: GiftValidationErrors = GiftValidationErrors(),
    val isSaved: Boolean = false
) {
    val isValid: Boolean
        get() = !validationErrors.hasErrors && 
                gift.name.isNotBlank() && 
                gift.recipient.isNotBlank() && 
                gift.price >= 0
}

/**
 * Validation errors for gift form
 */
data class GiftValidationErrors(
    val nameError: String? = null,
    val recipientError: String? = null,
    val priceError: String? = null
) {
    val hasErrors: Boolean
        get() = nameError != null || recipientError != null || priceError != null
}

/**
 * UI State for Gift Detail screen
 */
data class GiftDetailUiState(
    val gift: Gift? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val isDeleted: Boolean = false,
    val showDeleteConfirmation: Boolean = false
)

/**
 * One-time UI events
 */
sealed class GiftUiEvent {
    data class ShowSnackbar(val message: String) : GiftUiEvent()
    object NavigateBack : GiftUiEvent()
    data class NavigateToDetail(val giftId: Long) : GiftUiEvent()
    object NavigateToAddGift : GiftUiEvent()
}
