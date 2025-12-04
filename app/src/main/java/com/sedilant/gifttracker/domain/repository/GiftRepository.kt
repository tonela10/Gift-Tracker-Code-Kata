package com.sedilant.gifttracker.domain.repository

import com.sedilant.gifttracker.data.local.GiftStatus
import com.sedilant.gifttracker.data.local.Occasion
import com.sedilant.gifttracker.domain.model.Gift
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Gift operations
 * Follows the repository pattern for clean architecture
 */
interface GiftRepository {
    
    fun getAllGifts(): Flow<List<Gift>>
    
    fun getGiftById(id: Long): Flow<Gift?>
    
    suspend fun getGiftByIdOnce(id: Long): Gift?
    
    fun getFilteredGifts(
        query: String? = null,
        status: GiftStatus? = null,
        occasion: Occasion? = null,
        sortByRecipient: Boolean = false
    ): Flow<List<Gift>>
    
    fun getGiftsByRecipient(recipient: String): Flow<List<Gift>>
    
    fun getGiftsByStatus(status: GiftStatus): Flow<List<Gift>>
    
    fun getTotalSpent(): Flow<Double>
    
    fun getTotalBudget(): Flow<Double>
    
    fun getCountByStatus(status: GiftStatus): Flow<Int>
    
    fun getAllRecipients(): Flow<List<String>>
    
    suspend fun addGift(gift: Gift): Result<Long>
    
    suspend fun updateGift(gift: Gift): Result<Unit>
    
    suspend fun deleteGift(giftId: Long): Result<Unit>
    
    suspend fun updateGiftStatus(giftId: Long, status: GiftStatus): Result<Unit>
}
