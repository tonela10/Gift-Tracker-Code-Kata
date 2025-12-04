package com.sedilant.gifttracker.viewmodel

import com.sedilant.gifttracker.data.local.GiftStatus
import com.sedilant.gifttracker.data.local.Occasion
import com.sedilant.gifttracker.domain.model.Gift
import com.sedilant.gifttracker.ui.state.GiftUiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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
class GiftFormViewModelTest {
    
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: FakeGiftRepository
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeGiftRepository()
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `initial state for new gift is empty`() = runTest {
        val viewModel = createViewModel(giftId = null)
        advanceUntilIdle()
        
        val state = viewModel.uiState.value
        assertFalse(state.isEditing)
        assertEquals("", state.gift.name)
        assertEquals("", state.gift.recipient)
        assertEquals(0.0, state.gift.price, 0.01)
    }
    
    @Test
    fun `name change updates state`() = runTest {
        val viewModel = createViewModel(giftId = null)
        advanceUntilIdle()
        
        viewModel.onNameChange("New Gift")
        
        assertEquals("New Gift", viewModel.uiState.value.gift.name)
    }
    
    @Test
    fun `recipient change updates state`() = runTest {
        val viewModel = createViewModel(giftId = null)
        advanceUntilIdle()
        
        viewModel.onRecipientChange("John")
        
        assertEquals("John", viewModel.uiState.value.gift.recipient)
    }
    
    @Test
    fun `price change updates state`() = runTest {
        val viewModel = createViewModel(giftId = null)
        advanceUntilIdle()
        
        viewModel.onPriceChange("99.99")
        
        assertEquals(99.99, viewModel.uiState.value.gift.price, 0.01)
    }
    
    @Test
    fun `status change updates state`() = runTest {
        val viewModel = createViewModel(giftId = null)
        advanceUntilIdle()
        
        viewModel.onStatusChange(GiftStatus.PURCHASED)
        
        assertEquals(GiftStatus.PURCHASED, viewModel.uiState.value.gift.status)
    }
    
    @Test
    fun `occasion change updates state`() = runTest {
        val viewModel = createViewModel(giftId = null)
        advanceUntilIdle()
        
        viewModel.onOccasionChange(Occasion.CHRISTMAS)
        
        assertEquals(Occasion.CHRISTMAS, viewModel.uiState.value.gift.occasion)
    }
    
    @Test
    fun `save with empty name shows validation error`() = runTest {
        val viewModel = createViewModel(giftId = null)
        advanceUntilIdle()
        
        viewModel.onRecipientChange("John")
        viewModel.onPriceChange("50.00")
        viewModel.saveGift()
        advanceUntilIdle()
        
        val state = viewModel.uiState.value
        assertNotNull(state.validationErrors.nameError)
        assertFalse(state.isSaved)
    }
    
    @Test
    fun `save with empty recipient shows validation error`() = runTest {
        val viewModel = createViewModel(giftId = null)
        advanceUntilIdle()
        
        viewModel.onNameChange("Gift Name")
        viewModel.onPriceChange("50.00")
        viewModel.saveGift()
        advanceUntilIdle()
        
        val state = viewModel.uiState.value
        assertNotNull(state.validationErrors.recipientError)
        assertFalse(state.isSaved)
    }
    
    @Test
    fun `save valid gift adds to repository`() = runTest {
        val viewModel = createViewModel(giftId = null)
        advanceUntilIdle()
        
        viewModel.onNameChange("New Gift")
        viewModel.onRecipientChange("John")
        viewModel.onPriceChange("99.99")
        viewModel.saveGift()
        advanceUntilIdle()
        
        val state = viewModel.uiState.value
        assertTrue(state.isSaved)
        assertTrue(repository.testGifts.any { it.name == "New Gift" })
    }
    
    @Test
    fun `validation clears when field is edited`() = runTest {
        val viewModel = createViewModel(giftId = null)
        advanceUntilIdle()
        
        // Trigger validation error
        viewModel.saveGift()
        advanceUntilIdle()
        
        assertNotNull(viewModel.uiState.value.validationErrors.nameError)
        
        // Edit field should clear error
        viewModel.onNameChange("Name")
        
        assertNull(viewModel.uiState.value.validationErrors.nameError)
    }
    
    private fun createViewModel(giftId: Long?): GiftFormViewModel {
        val savedStateHandle = androidx.lifecycle.SavedStateHandle().apply {
            giftId?.let { set("giftId", it) }
        }
        return GiftFormViewModel(repository, savedStateHandle)
    }
}
