package com.sedilant.gifttracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gifts")
data class GiftEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val person: String,
    val price: Float,
    val isPurchased: Boolean = false
)
