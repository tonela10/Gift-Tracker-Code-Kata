package com.sedilant.gifttracker.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Gift operations
 */
@Dao
interface GiftDao {
    
    @Query("SELECT * FROM gifts ORDER BY createdAt DESC")
    fun getAllGifts(): Flow<List<GiftEntity>>
    
    @Query("SELECT * FROM gifts WHERE id = :id")
    fun getGiftById(id: Long): Flow<GiftEntity?>
    
    @Query("SELECT * FROM gifts WHERE id = :id")
    suspend fun getGiftByIdOnce(id: Long): GiftEntity?
    
    @Query("""
        SELECT * FROM gifts 
        WHERE (:query IS NULL OR name LIKE '%' || :query || '%' OR recipient LIKE '%' || :query || '%')
        AND (:status IS NULL OR status = :status)
        AND (:occasion IS NULL OR occasion = :occasion)
        ORDER BY 
            CASE WHEN :sortByRecipient = 1 THEN recipient END ASC,
            CASE WHEN :sortByRecipient = 0 THEN createdAt END DESC
    """)
    fun getFilteredGifts(
        query: String? = null,
        status: GiftStatus? = null,
        occasion: Occasion? = null,
        sortByRecipient: Boolean = false
    ): Flow<List<GiftEntity>>
    
    @Query("SELECT * FROM gifts WHERE recipient = :recipient ORDER BY createdAt DESC")
    fun getGiftsByRecipient(recipient: String): Flow<List<GiftEntity>>
    
    @Query("SELECT * FROM gifts WHERE status = :status ORDER BY createdAt DESC")
    fun getGiftsByStatus(status: GiftStatus): Flow<List<GiftEntity>>
    
    @Query("SELECT SUM(price) FROM gifts WHERE status != :excludeStatus")
    fun getTotalSpent(excludeStatus: GiftStatus = GiftStatus.IDEA): Flow<Double?>
    
    @Query("SELECT SUM(price) FROM gifts")
    fun getTotalBudget(): Flow<Double?>
    
    @Query("SELECT COUNT(*) FROM gifts WHERE status = :status")
    fun getCountByStatus(status: GiftStatus): Flow<Int>
    
    @Query("SELECT DISTINCT recipient FROM gifts ORDER BY recipient ASC")
    fun getAllRecipients(): Flow<List<String>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGift(gift: GiftEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGifts(gifts: List<GiftEntity>)
    
    @Update
    suspend fun updateGift(gift: GiftEntity)
    
    @Delete
    suspend fun deleteGift(gift: GiftEntity)
    
    @Query("DELETE FROM gifts WHERE id = :id")
    suspend fun deleteGiftById(id: Long)
    
    @Query("DELETE FROM gifts")
    suspend fun deleteAllGifts()
}
