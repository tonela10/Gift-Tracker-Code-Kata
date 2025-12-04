package com.sedilant.gifttracker.data.local

import androidx.room.TypeConverter
import java.time.LocalDate

/**
 * Type converters for Room database
 */
class Converters {
    
    @TypeConverter
    fun fromGiftStatus(status: GiftStatus): String = status.name
    
    @TypeConverter
    fun toGiftStatus(value: String): GiftStatus = GiftStatus.valueOf(value)
    
    @TypeConverter
    fun fromOccasion(occasion: Occasion): String = occasion.name
    
    @TypeConverter
    fun toOccasion(value: String): Occasion = Occasion.valueOf(value)
    
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): Long? = date?.toEpochDay()
    
    @TypeConverter
    fun toLocalDate(epochDay: Long?): LocalDate? = epochDay?.let { LocalDate.ofEpochDay(it) }
}
