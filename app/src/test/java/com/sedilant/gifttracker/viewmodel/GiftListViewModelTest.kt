package com.sedilant.gifttracker.viewmodel

import com.sedilant.gifttracker.data.local.GiftStatus
import com.sedilant.gifttracker.data.local.Occasion
import com.sedilant.gifttracker.domain.model.Gift
import com.sedilant.gifttracker.domain.repository.GiftRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GiftListViewModelTest {
    
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: FakeGiftRepository
    private lateinit var viewModel: GiftListViewModel
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeGiftRepository()
        viewModel = GiftListViewModel(repository)
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `initial state shows loading then gifts`() = runTest {
        advanceUntilIdle()
        
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(3, state.gifts.size)
    }
    
    @Test
    fun `search query filters gifts by name`() = runTest {
        advanceUntilIdle()
        
        viewModel.onSearchQueryChange("Watch")
        advanceUntilIdle()
        
        val state = viewModel.uiState.value
        assertEquals("Watch", state.searchQuery)
        assertEquals(1, state.filteredGifts.size)
        assertEquals("Smart Watch", state.filteredGifts.first().name)
    }
    
    @Test
    fun `search query filters gifts by recipient`() = runTest {
        advanceUntilIdle()
        
        viewModel.onSearchQueryChange("John")
        advanceUntilIdle()
        
        val state = viewModel.uiState.value
        assertEquals(1, state.filteredGifts.size)
        assertEquals("John", state.filteredGifts.first().recipient)
    }
    
    @Test
    fun `status filter shows only matching gifts`() = runTest {
        advanceUntilIdle()
        
        viewModel.onStatusFilterChange(GiftStatus.PURCHASED)
        advanceUntilIdle()
        
        val state = viewModel.uiState.value
        assertEquals(GiftStatus.PURCHASED, state.statusFilter)
        assertTrue(state.filteredGifts.all { it.status == GiftStatus.PURCHASED })
    }
    
    @Test
    fun `clear filters resets all filters`() = runTest {
        advanceUntilIdle()
        
        viewModel.onSearchQueryChange("test")
        viewModel.onStatusFilterChange(GiftStatus.IDEA)
        advanceUntilIdle()
        
        viewModel.clearFilters()
        advanceUntilIdle()
        
        val state = viewModel.uiState.value
        assertEquals("", state.searchQuery)
        assertNull(state.statusFilter)
        assertNull(state.occasionFilter)
    }
    
    @Test
    fun `gift stats are calculated correctly`() = runTest {
        advanceUntilIdle()
        
        val state = viewModel.uiState.value
        assertEquals(3, state.giftStats.totalGifts)
        assertEquals(1, state.giftStats.ideasCount)
        assertEquals(1, state.giftStats.purchasedCount)
        assertEquals(1, state.giftStats.wrappedCount)
    }
    
    @Test
    fun `delete gift removes from list`() = runTest {
        advanceUntilIdle()
        
        val giftToDelete = repository.testGifts.first()
        viewModel.onDeleteGift(giftToDelete.id)
        advanceUntilIdle()
        
        val state = viewModel.uiState.value
        assertFalse(state.gifts.any { it.id == giftToDelete.id })
    }
}

/**
 * Fake repository for testing
 */
class FakeGiftRepository : GiftRepository {
    
    val testGifts = mutableListOf(
        Gift(
            id = 1,
            name = "Smart Watch",
            recipient = "John",
            price = 299.99,
            status = GiftStatus.IDEA,
            occasion = Occasion.BIRTHDAY
        ),
        Gift(
            id = 2,
            name = "Book Set",
            recipient = "Sarah",
            price = 49.99,
            status = GiftStatus.PURCHASED,
            occasion = Occasion.CHRISTMAS
        ),
        Gift(
            id = 3,
            name = "Coffee Maker",
            recipient = "Mom",
            price = 89.99,
            status = GiftStatus.WRAPPED,
            occasion = Occasion.MOTHERS_DAY
        )
    )
    
    private val giftsFlow = MutableStateFlow(testGifts.toList())
    
    override fun getAllGifts(): Flow<List<Gift>> = giftsFlow
    
    override fun getGiftById(id: Long): Flow<Gift?> = flowOf(testGifts.find { it.id == id })
    
    override suspend fun getGiftByIdOnce(id: Long): Gift? = testGifts.find { it.id == id }
    
    override fun getFilteredGifts(
        query: String?,
        status: GiftStatus?,
        occasion: Occasion?,
        sortByRecipient: Boolean
    ): Flow<List<Gift>> = flowOf(
        testGifts.filter { gift ->
            (query.isNullOrEmpty() || gift.name.contains(query, true) || gift.recipient.contains(query, true)) &&
            (status == null || gift.status == status) &&
            (occasion == null || gift.occasion == occasion)
        }
    )
    
    override fun getGiftsByRecipient(recipient: String): Flow<List<Gift>> = 
        flowOf(testGifts.filter { it.recipient == recipient })
    
    override fun getGiftsByStatus(status: GiftStatus): Flow<List<Gift>> = 
        flowOf(testGifts.filter { it.status == status })
    
    override fun getTotalSpent(): Flow<Double> = 
        flowOf(testGifts.filter { it.status != GiftStatus.IDEA }.sumOf { it.price })
    
    override fun getTotalBudget(): Flow<Double> = 
        flowOf(testGifts.sumOf { it.price })
    
    override fun getCountByStatus(status: GiftStatus): Flow<Int> = 
        flowOf(testGifts.count { it.status == status })
    
    override fun getAllRecipients(): Flow<List<String>> = 
        flowOf(testGifts.map { it.recipient }.distinct())
    
    override suspend fun addGift(gift: Gift): Result<Long> {
        val newId = (testGifts.maxOfOrNull { it.id } ?: 0) + 1
        testGifts.add(gift.copy(id = newId))
        giftsFlow.value = testGifts.toList()
        return Result.success(newId)
    }
    
    override suspend fun updateGift(gift: Gift): Result<Unit> {
        val index = testGifts.indexOfFirst { it.id == gift.id }
        if (index != -1) {
            testGifts[index] = gift
            giftsFlow.value = testGifts.toList()
        }
        return Result.success(Unit)
    }
    
    override suspend fun deleteGift(giftId: Long): Result<Unit> {
        testGifts.removeIf { it.id == giftId }
        giftsFlow.value = testGifts.toList()
        return Result.success(Unit)
    }
    
    override suspend fun updateGiftStatus(giftId: Long, status: GiftStatus): Result<Unit> {
        val index = testGifts.indexOfFirst { it.id == giftId }
        if (index != -1) {
            testGifts[index] = testGifts[index].copy(status = status)
            giftsFlow.value = testGifts.toList()
        }
        return Result.success(Unit)
    }
}
