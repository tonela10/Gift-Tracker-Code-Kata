package com.sedilant.gifttracker.domain.model

import com.sedilant.gifttracker.data.local.GiftStatus
import com.sedilant.gifttracker.data.local.Occasion
import java.time.LocalDate

/**
 * Domain model for Gift
 * This is the model used throughout the app's business logic
 */
data class Gift(
    val id: Long = 0,
    val name: String,
    val recipient: String,
    val price: Double,
    val status: GiftStatus = GiftStatus.IDEA,
    val occasion: Occasion = Occasion.OTHER,
    val eventDate: LocalDate? = null,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val isPurchased: Boolean
        get() = status != GiftStatus.IDEA
    
    val isCompleted: Boolean
        get() = status == GiftStatus.GIVEN
    
    fun nextStatus(): GiftStatus? = when (status) {
        GiftStatus.IDEA -> GiftStatus.PURCHASED
        GiftStatus.PURCHASED -> GiftStatus.WRAPPED
        GiftStatus.WRAPPED -> GiftStatus.GIVEN
        GiftStatus.GIVEN -> null
    }
    
    fun previousStatus(): GiftStatus? = when (status) {
        GiftStatus.IDEA -> null
        GiftStatus.PURCHASED -> GiftStatus.IDEA
        GiftStatus.WRAPPED -> GiftStatus.PURCHASED
        GiftStatus.GIVEN -> GiftStatus.WRAPPED
    }
    
    companion object {
        val EMPTY = Gift(
            name = "",
            recipient = "",
            price = 0.0
        )
    }
}
