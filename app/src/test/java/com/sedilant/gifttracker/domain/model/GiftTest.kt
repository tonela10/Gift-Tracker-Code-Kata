package com.sedilant.gifttracker.domain.model

import com.sedilant.gifttracker.data.local.GiftStatus
import com.sedilant.gifttracker.data.local.Occasion
import org.junit.Assert.*
import org.junit.Test

class GiftTest {
    
    @Test
    fun `isPurchased returns false for IDEA status`() {
        val gift = createGift(status = GiftStatus.IDEA)
        assertFalse(gift.isPurchased)
    }
    
    @Test
    fun `isPurchased returns true for PURCHASED status`() {
        val gift = createGift(status = GiftStatus.PURCHASED)
        assertTrue(gift.isPurchased)
    }
    
    @Test
    fun `isPurchased returns true for WRAPPED status`() {
        val gift = createGift(status = GiftStatus.WRAPPED)
        assertTrue(gift.isPurchased)
    }
    
    @Test
    fun `isPurchased returns true for GIVEN status`() {
        val gift = createGift(status = GiftStatus.GIVEN)
        assertTrue(gift.isPurchased)
    }
    
    @Test
    fun `isCompleted returns true only for GIVEN status`() {
        assertFalse(createGift(status = GiftStatus.IDEA).isCompleted)
        assertFalse(createGift(status = GiftStatus.PURCHASED).isCompleted)
        assertFalse(createGift(status = GiftStatus.WRAPPED).isCompleted)
        assertTrue(createGift(status = GiftStatus.GIVEN).isCompleted)
    }
    
    @Test
    fun `nextStatus returns correct next status`() {
        assertEquals(GiftStatus.PURCHASED, createGift(status = GiftStatus.IDEA).nextStatus())
        assertEquals(GiftStatus.WRAPPED, createGift(status = GiftStatus.PURCHASED).nextStatus())
        assertEquals(GiftStatus.GIVEN, createGift(status = GiftStatus.WRAPPED).nextStatus())
        assertNull(createGift(status = GiftStatus.GIVEN).nextStatus())
    }
    
    @Test
    fun `previousStatus returns correct previous status`() {
        assertNull(createGift(status = GiftStatus.IDEA).previousStatus())
        assertEquals(GiftStatus.IDEA, createGift(status = GiftStatus.PURCHASED).previousStatus())
        assertEquals(GiftStatus.PURCHASED, createGift(status = GiftStatus.WRAPPED).previousStatus())
        assertEquals(GiftStatus.WRAPPED, createGift(status = GiftStatus.GIVEN).previousStatus())
    }
    
    @Test
    fun `EMPTY gift has default values`() {
        val empty = Gift.EMPTY
        assertEquals("", empty.name)
        assertEquals("", empty.recipient)
        assertEquals(0.0, empty.price, 0.01)
        assertEquals(GiftStatus.IDEA, empty.status)
        assertEquals(Occasion.OTHER, empty.occasion)
    }
    
    @Test
    fun `status displayName includes emoji`() {
        assertTrue(GiftStatus.IDEA.displayName.contains("💡"))
        assertTrue(GiftStatus.PURCHASED.displayName.contains("🛒"))
        assertTrue(GiftStatus.WRAPPED.displayName.contains("🎀"))
        assertTrue(GiftStatus.GIVEN.displayName.contains("🎁"))
    }
    
    @Test
    fun `occasion displayName includes emoji`() {
        assertTrue(Occasion.BIRTHDAY.displayName.contains("🎂"))
        assertTrue(Occasion.CHRISTMAS.displayName.contains("🎄"))
        assertTrue(Occasion.WEDDING.displayName.contains("💒"))
    }
    
    private fun createGift(
        status: GiftStatus = GiftStatus.IDEA,
        occasion: Occasion = Occasion.OTHER
    ) = Gift(
        id = 1,
        name = "Test Gift",
        recipient = "Test Recipient",
        price = 100.0,
        status = status,
        occasion = occasion
    )
}
