package com.sedilant.gifttracker.data.di

import android.content.Context
import androidx.room.Room
import com.sedilant.gifttracker.data.local.GiftDatabase
import com.sedilant.gifttracker.data.local.dao.GiftDao
import com.sedilant.gifttracker.data.repository.GiftRepository
import com.sedilant.gifttracker.data.repository.GiftRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideGiftDatabase(
        @ApplicationContext context: Context
    ): GiftDatabase {
        return Room.databaseBuilder(
            context,
            GiftDatabase::class.java,
            "gift_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideGiftDao(database: GiftDatabase): GiftDao {
        return database.giftDao()
    }

    @Provides
    @Singleton
    fun provideGiftRepository(giftDao: GiftDao): GiftRepository {
        return GiftRepositoryImpl(giftDao)
    }
}
