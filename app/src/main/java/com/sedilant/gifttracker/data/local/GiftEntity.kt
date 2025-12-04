package com.sedilant.gifttracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * Gift status representing the lifecycle of a gift
 */
enum class GiftStatus {
    IDEA,
    PURCHASED,
    WRAPPED,
    GIVEN;
    
    val displayName: String
        get() = when (this) {
            IDEA -> "💡 Idea"
            PURCHASED -> "🛒 Purchased"
            WRAPPED -> "🎀 Wrapped"
            GIVEN -> "🎁 Given"
        }
    
    val emoji: String
        get() = when (this) {
            IDEA -> "💡"
            PURCHASED -> "🛒"
            WRAPPED -> "🎀"
            GIVEN -> "🎁"
        }
}

/**
 * Occasion for gift-giving
 */
enum class Occasion {
    BIRTHDAY,
    CHRISTMAS,
    ANNIVERSARY,
    WEDDING,
    GRADUATION,
    VALENTINES,
    MOTHERS_DAY,
    FATHERS_DAY,
    OTHER;
    
    val displayName: String
        get() = when (this) {
            BIRTHDAY -> "🎂 Birthday"
            CHRISTMAS -> "🎄 Christmas"
            ANNIVERSARY -> "💕 Anniversary"
            WEDDING -> "💒 Wedding"
            GRADUATION -> "🎓 Graduation"
            VALENTINES -> "❤️ Valentine's Day"
            MOTHERS_DAY -> "👩 Mother's Day"
            FATHERS_DAY -> "👨 Father's Day"
            OTHER -> "🎁 Other"
        }
}

/**
 * Gift entity for Room database
 */
@Entity(tableName = "gifts")
data class GiftEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val recipient: String,
    val price: Double,
    val status: GiftStatus = GiftStatus.IDEA,
    val occasion: Occasion = Occasion.OTHER,
    val eventDate: LocalDate? = null,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
