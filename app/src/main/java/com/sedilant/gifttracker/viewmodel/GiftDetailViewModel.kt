package com.sedilant.gifttracker.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sedilant.gifttracker.data.local.GiftStatus
import com.sedilant.gifttracker.domain.repository.GiftRepository
import com.sedilant.gifttracker.ui.state.GiftDetailUiState
import com.sedilant.gifttracker.ui.state.GiftUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel for Gift Detail screen
 */
@HiltViewModel
class GiftDetailViewModel @Inject constructor(
    private val repository: GiftRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val giftId: Long = savedStateHandle.get<Long>("giftId") ?: 0L
    
    private val _uiState = MutableStateFlow(GiftDetailUiState())
    val uiState: StateFlow<GiftDetailUiState> = _uiState.asStateFlow()
    
    private val _uiEvent = Channel<GiftUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
    
    init {
        loadGift()
    }
    
    private fun loadGift() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                repository.getGiftById(giftId).collect { gift ->
                    _uiState.update { 
                        it.copy(
                            gift = gift,
                            isLoading = false,
                            error = if (gift == null) "Gift not found" else null
                        )
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Failed to load gift")
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
    
    fun onUpdateStatus(status: GiftStatus) {
        viewModelScope.launch {
            repository.updateGiftStatus(giftId, status).fold(
                onSuccess = {
                    _uiEvent.send(GiftUiEvent.ShowSnackbar("Status updated to ${status.displayName}"))
                },
                onFailure = { e ->
                    Timber.e(e, "Failed to update status")
                    _uiEvent.send(GiftUiEvent.ShowSnackbar("Failed to update status"))
                }
            )
        }
    }
    
    fun onAdvanceStatus() {
        _uiState.value.gift?.nextStatus()?.let { nextStatus ->
            onUpdateStatus(nextStatus)
        }
    }
    
    fun onShowDeleteConfirmation() {
        _uiState.update { it.copy(showDeleteConfirmation = true) }
    }
    
    fun onDismissDeleteConfirmation() {
        _uiState.update { it.copy(showDeleteConfirmation = false) }
    }
    
    fun onDeleteGift() {
        viewModelScope.launch {
            _uiState.update { it.copy(showDeleteConfirmation = false) }
            
            repository.deleteGift(giftId).fold(
                onSuccess = {
                    _uiState.update { it.copy(isDeleted = true) }
                    _uiEvent.send(GiftUiEvent.ShowSnackbar("Gift deleted"))
                    _uiEvent.send(GiftUiEvent.NavigateBack)
                },
                onFailure = { e ->
                    Timber.e(e, "Failed to delete gift")
                    _uiEvent.send(GiftUiEvent.ShowSnackbar("Failed to delete gift"))
                }
            )
        }
    }
}
