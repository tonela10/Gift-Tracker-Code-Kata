package com.sedilant.gifttracker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Room database for Gift Tracker
 */
@Database(
    entities = [GiftEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class GiftDatabase : RoomDatabase() {
    
    abstract fun giftDao(): GiftDao
    
    companion object {
        private const val DATABASE_NAME = "gift_tracker_db"
        
        @Volatile
        private var INSTANCE: GiftDatabase? = null
        
        fun getInstance(context: Context): GiftDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GiftDatabase::class.java,
                    DATABASE_NAME
                )
                    .addCallback(DatabaseCallback())
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
    
    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateSampleData(database.giftDao())
                }
            }
        }
        
        private suspend fun populateSampleData(dao: GiftDao) {
            val sampleGifts = listOf(
                GiftEntity(
                    name = "Smart Watch",
                    recipient = "John",
                    price = 299.99,
                    status = GiftStatus.IDEA,
                    occasion = Occasion.BIRTHDAY,
                    notes = "Black color preferred"
                ),
                GiftEntity(
                    name = "Book Set",
                    recipient = "Sarah",
                    price = 49.99,
                    status = GiftStatus.PURCHASED,
                    occasion = Occasion.CHRISTMAS,
                    notes = "Fantasy series"
                ),
                GiftEntity(
                    name = "Coffee Maker",
                    recipient = "Mom",
                    price = 89.99,
                    status = GiftStatus.WRAPPED,
                    occasion = Occasion.MOTHERS_DAY,
                    notes = "With milk frother"
                ),
                GiftEntity(
                    name = "Headphones",
                    recipient = "Dad",
                    price = 149.99,
                    status = GiftStatus.GIVEN,
                    occasion = Occasion.FATHERS_DAY,
                    notes = "Noise cancelling"
                )
            )
            dao.insertGifts(sampleGifts)
        }
    }
}
