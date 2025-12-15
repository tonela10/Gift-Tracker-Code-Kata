package com.sedilant.gifttracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sedilant.gifttracker.data.local.dao.GiftDao
import com.sedilant.gifttracker.data.local.entity.GiftEntity

@Database(
    entities = [GiftEntity::class],
    version = 1,
    exportSchema = false
)
abstract class GiftDatabase : RoomDatabase() {
    abstract fun giftDao(): GiftDao
}
