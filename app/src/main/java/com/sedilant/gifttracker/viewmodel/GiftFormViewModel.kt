package com.sedilant.gifttracker.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sedilant.gifttracker.data.local.GiftStatus
import com.sedilant.gifttracker.data.local.Occasion
import com.sedilant.gifttracker.domain.model.Gift
import com.sedilant.gifttracker.domain.repository.GiftRepository
import com.sedilant.gifttracker.ui.state.GiftFormUiState
import com.sedilant.gifttracker.ui.state.GiftUiEvent
import com.sedilant.gifttracker.ui.state.GiftValidationErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

/**
 * ViewModel for Add/Edit Gift screen
 */
@HiltViewModel
class GiftFormViewModel @Inject constructor(
    private val repository: GiftRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val giftId: Long? = savedStateHandle.get<Long>("giftId")?.takeIf { it > 0 }
    
    private val _uiState = MutableStateFlow(GiftFormUiState(isEditing = giftId != null))
    val uiState: StateFlow<GiftFormUiState> = _uiState.asStateFlow()
    
    private val _uiEvent = Channel<GiftUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
    
    init {
        giftId?.let { loadGift(it) }
    }
    
    private fun loadGift(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val gift = repository.getGiftByIdOnce(id)
                if (gift != null) {
                    _uiState.update { 
                        it.copy(
                            gift = gift,
                            isLoading = false,
                            isEditing = true
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Gift not found") }
                    _uiEvent.send(GiftUiEvent.NavigateBack)
                }
            } catch (e: Exception) {
                Timber.e(e, "Failed to load gift")
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
    
    fun onNameChange(name: String) {
        _uiState.update { state ->
            state.copy(
                gift = state.gift.copy(name = name),
                validationErrors = state.validationErrors.copy(nameError = null)
            )
        }
    }
    
    fun onRecipientChange(recipient: String) {
        _uiState.update { state ->
            state.copy(
                gift = state.gift.copy(recipient = recipient),
                validationErrors = state.validationErrors.copy(recipientError = null)
            )
        }
    }
    
    fun onPriceChange(priceText: String) {
        val price = priceText.toDoubleOrNull() ?: 0.0
        _uiState.update { state ->
            state.copy(
                gift = state.gift.copy(price = price),
                validationErrors = state.validationErrors.copy(priceError = null)
            )
        }
    }
    
    fun onStatusChange(status: GiftStatus) {
        _uiState.update { state ->
            state.copy(gift = state.gift.copy(status = status))
        }
    }
    
    fun onOccasionChange(occasion: Occasion) {
        _uiState.update { state ->
            state.copy(gift = state.gift.copy(occasion = occasion))
        }
    }
    
    fun onEventDateChange(date: LocalDate?) {
        _uiState.update { state ->
            state.copy(gift = state.gift.copy(eventDate = date))
        }
    }
    
    fun onNotesChange(notes: String) {
        _uiState.update { state ->
            state.copy(gift = state.gift.copy(notes = notes))
        }
    }
    
    fun saveGift() {
        val currentState = _uiState.value
        val gift = currentState.gift
        
        // Validate
        val validationErrors = validate(gift)
        if (validationErrors.hasErrors) {
            _uiState.update { it.copy(validationErrors = validationErrors) }
            return
        }
        
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            
            val result = if (currentState.isEditing) {
                repository.updateGift(gift.copy(updatedAt = System.currentTimeMillis()))
            } else {
                repository.addGift(gift).map { }
            }
            
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isSaving = false, isSaved = true) }
                    _uiEvent.send(GiftUiEvent.ShowSnackbar(
                        if (currentState.isEditing) "Gift updated" else "Gift added"
                    ))
                    _uiEvent.send(GiftUiEvent.NavigateBack)
                },
                onFailure = { e ->
                    Timber.e(e, "Failed to save gift")
                    _uiState.update { 
                        it.copy(isSaving = false, error = e.message ?: "Failed to save gift")
                    }
                    _uiEvent.send(GiftUiEvent.ShowSnackbar("Failed to save gift"))
                }
            )
        }
    }
    
    private fun validate(gift: Gift): GiftValidationErrors {
        return GiftValidationErrors(
            nameError = if (gift.name.isBlank()) "Name is required" else null,
            recipientError = if (gift.recipient.isBlank()) "Recipient is required" else null,
            priceError = if (gift.price < 0) "Price cannot be negative" else null
        )
    }
    
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
