package com.sedilant.gifttracker.di

import android.content.Context
import com.sedilant.gifttracker.data.local.GiftDao
import com.sedilant.gifttracker.data.local.GiftDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for database dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideGiftDatabase(@ApplicationContext context: Context): GiftDatabase {
        return GiftDatabase.getInstance(context)
    }
    
    @Provides
    @Singleton
    fun provideGiftDao(database: GiftDatabase): GiftDao {
        return database.giftDao()
    }
}
