package com.sedilant.gifttracker.data

import java.util.UUID

/**
 * Represents a gift item in the tracker
 */
data class Gift(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val recipient: String,
    val price: Double,
    val isPurchased: Boolean = false,
    val notes: String = ""
)
