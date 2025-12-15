package com.sedilant.gifttracker.data.repository

import com.sedilant.gifttracker.data.local.entity.GiftEntity
import kotlinx.coroutines.flow.Flow

interface GiftRepository {
    fun getAllGifts(): Flow<List<GiftEntity>>
    suspend fun getGiftById(id: Long): GiftEntity?
    suspend fun insertGift(gift: GiftEntity): Long
    suspend fun updateGift(gift: GiftEntity)
    suspend fun deleteGift(gift: GiftEntity)
    suspend fun deleteGiftById(id: Long)
    suspend fun updatePurchasedStatus(id: Long, isPurchased: Boolean)
}
