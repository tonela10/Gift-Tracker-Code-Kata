package com.sedilant.gifttracker.data.repository

import com.sedilant.gifttracker.data.local.dao.GiftDao
import com.sedilant.gifttracker.data.local.entity.GiftEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GiftRepositoryImpl @Inject constructor(
    private val giftDao: GiftDao
) : GiftRepository {

    override fun getAllGifts(): Flow<List<GiftEntity>> = giftDao.getAllGifts()

    override suspend fun getGiftById(id: Long): GiftEntity? = giftDao.getGiftById(id)

    override suspend fun insertGift(gift: GiftEntity): Long = giftDao.insertGift(gift)

    override suspend fun updateGift(gift: GiftEntity) = giftDao.updateGift(gift)

    override suspend fun deleteGift(gift: GiftEntity) = giftDao.deleteGift(gift)

    override suspend fun deleteGiftById(id: Long) = giftDao.deleteGiftById(id)

    override suspend fun updatePurchasedStatus(id: Long, isPurchased: Boolean) =
        giftDao.updatePurchasedStatus(id, isPurchased)
}
