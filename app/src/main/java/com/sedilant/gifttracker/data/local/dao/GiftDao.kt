package com.sedilant.gifttracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sedilant.gifttracker.data.local.entity.GiftEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GiftDao {

    @Query("SELECT * FROM gifts")
    fun getAllGifts(): Flow<List<GiftEntity>>

    @Query("SELECT * FROM gifts WHERE id = :id")
    suspend fun getGiftById(id: Long): GiftEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGift(gift: GiftEntity): Long

    @Update
    suspend fun updateGift(gift: GiftEntity)

    @Delete
    suspend fun deleteGift(gift: GiftEntity)

    @Query("DELETE FROM gifts WHERE id = :id")
    suspend fun deleteGiftById(id: Long)

    @Query("UPDATE gifts SET isPurchased = :isPurchased WHERE id = :id")
    suspend fun updatePurchasedStatus(id: Long, isPurchased: Boolean)
}
