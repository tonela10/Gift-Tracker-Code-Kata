package com.sedilant.gifttracker.data.repository

import com.sedilant.gifttracker.data.local.GiftDao
import com.sedilant.gifttracker.data.local.GiftEntity
import com.sedilant.gifttracker.data.local.GiftStatus
import com.sedilant.gifttracker.data.local.Occasion
import com.sedilant.gifttracker.domain.model.Gift
import com.sedilant.gifttracker.domain.repository.GiftRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of GiftRepository using Room database
 */
@Singleton
class GiftRepositoryImpl @Inject constructor(
    private val giftDao: GiftDao
) : GiftRepository {
    
    override fun getAllGifts(): Flow<List<Gift>> {
        return giftDao.getAllGifts().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getGiftById(id: Long): Flow<Gift?> {
        return giftDao.getGiftById(id).map { it?.toDomainModel() }
    }
    
    override suspend fun getGiftByIdOnce(id: Long): Gift? {
        return giftDao.getGiftByIdOnce(id)?.toDomainModel()
    }
    
    override fun getFilteredGifts(
        query: String?,
        status: GiftStatus?,
        occasion: Occasion?,
        sortByRecipient: Boolean
    ): Flow<List<Gift>> {
        return giftDao.getFilteredGifts(query, status, occasion, sortByRecipient).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getGiftsByRecipient(recipient: String): Flow<List<Gift>> {
        return giftDao.getGiftsByRecipient(recipient).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getGiftsByStatus(status: GiftStatus): Flow<List<Gift>> {
        return giftDao.getGiftsByStatus(status).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getTotalSpent(): Flow<Double> {
        return giftDao.getTotalSpent().map { it ?: 0.0 }
    }
    
    override fun getTotalBudget(): Flow<Double> {
        return giftDao.getTotalBudget().map { it ?: 0.0 }
    }
    
    override fun getCountByStatus(status: GiftStatus): Flow<Int> {
        return giftDao.getCountByStatus(status)
    }
    
    override fun getAllRecipients(): Flow<List<String>> {
        return giftDao.getAllRecipients()
    }
    
    override suspend fun addGift(gift: Gift): Result<Long> {
        return try {
            val id = giftDao.insertGift(gift.toEntity())
            Timber.d("Gift added with id: $id")
            Result.success(id)
        } catch (e: Exception) {
            Timber.e(e, "Failed to add gift")
            Result.failure(e)
        }
    }
    
    override suspend fun updateGift(gift: Gift): Result<Unit> {
        return try {
            giftDao.updateGift(gift.toEntity())
            Timber.d("Gift updated: ${gift.id}")
            Result.success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Failed to update gift")
            Result.failure(e)
        }
    }
    
    override suspend fun deleteGift(giftId: Long): Result<Unit> {
        return try {
            giftDao.deleteGiftById(giftId)
            Timber.d("Gift deleted: $giftId")
            Result.success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Failed to delete gift")
            Result.failure(e)
        }
    }
    
    override suspend fun updateGiftStatus(giftId: Long, status: GiftStatus): Result<Unit> {
        return try {
            val gift = giftDao.getGiftByIdOnce(giftId)
            gift?.let {
                giftDao.updateGift(it.copy(status = status, updatedAt = System.currentTimeMillis()))
                Timber.d("Gift status updated: $giftId -> $status")
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Failed to update gift status")
            Result.failure(e)
        }
    }
}

// Extension functions for mapping
private fun GiftEntity.toDomainModel(): Gift = Gift(
    id = id,
    name = name,
    recipient = recipient,
    price = price,
    status = status,
    occasion = occasion,
    eventDate = eventDate,
    notes = notes,
    createdAt = createdAt,
    updatedAt = updatedAt
)

private fun Gift.toEntity(): GiftEntity = GiftEntity(
    id = id,
    name = name,
    recipient = recipient,
    price = price,
    status = status,
    occasion = occasion,
    eventDate = eventDate,
    notes = notes,
    createdAt = createdAt,
    updatedAt = updatedAt
)
