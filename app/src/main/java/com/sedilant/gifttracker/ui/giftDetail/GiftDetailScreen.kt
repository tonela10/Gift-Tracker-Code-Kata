package com.sedilant.gifttracker.ui.giftDetail

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sedilant.gifttracker.ui.theme.BackgroundLight

@Composable
public fun GiftDetailScreen(
    itemId: String?,
    viewModel: GiftDetailViewModel = hiltViewModel(
        creationCallback = { factory: GiftDetailViewModelFactory ->
            factory.create(itemId = itemId)
        }),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    GiftDetailScreenStateless(
        uiState = uiState,
        onToggleEditMode = viewModel::toggleEditMode,
        onSaveGiftDetails = viewModel::saveGiftDetails,
        onNavigateBack = onNavigateBack,
        onPersonChange = viewModel::onPersonChange,
        onGiftChange = viewModel::onGiftChange,
        onPriceChange = viewModel::onPriceChange
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun GiftDetailScreenStateless(
    uiState: GiftDetailUiState,
    onToggleEditMode: () -> Unit,
    onSaveGiftDetails: () -> Unit,
    onNavigateBack: () -> Unit,
    onPersonChange: (String) -> Unit,
    onGiftChange: (String) -> Unit,
    onPriceChange: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.background(BackgroundLight),
                title = { Text("Detalle del Regalo", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    // TODO change the animation
                    Crossfade(
                        targetState = uiState.isEditMode,
                        label = "nav-icon-animation"
                    ) { isEditMode ->
                        if (isEditMode) {
                            IconButton(onClick = { onToggleEditMode() }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Salir de edición"
                                )
                            }
                        } else {
                            IconButton(onClick = onNavigateBack) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Volver"
                                )
                            }
                        }
                    }
                }
            )
        },
        bottomBar = {
            // TODO change the animation for another cool one
            Crossfade(
                targetState = uiState.isEditMode,
                label = "bottom-bar-animation"
            ) { isEditMode ->
                val buttonColor = if (isEditMode) Color(0xFF4CAF50) else Color(0xFFC62828)
                val buttonText = if (isEditMode) "Guardar" else "Editar"
                val onClickAction = if (isEditMode) {
                    { onSaveGiftDetails() }
                } else {
                    { onToggleEditMode() }
                }

                Button(
                    onClick = onClickAction,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text(
                        text = buttonText,
                        modifier = Modifier.padding(8.dp),
                        fontSize = 16.sp
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(BackgroundLight)
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DetailItemCard(
                label = "Persona",
                value = uiState.person,
                isEditMode = uiState.isEditMode,
                onValueChange = { onPersonChange(it) }
            )
            DetailItemCard(
                label = "Regalo",
                value = uiState.gift,
                isEditMode = uiState.isEditMode,
                onValueChange = { onGiftChange(it) }
            )
            DetailItemCard(
                label = "Precio en €",
                value = uiState.price,
                isEditMode = uiState.isEditMode,
                onValueChange = { onPriceChange(it) }
            )
            StatusItemCard(isPurchased = uiState.isPurchased)
        }
    }
}

@Preview
@Composable
private fun GiftDetailScreenPreview() {
    GiftDetailScreenStateless(
        uiState = GiftDetailUiState(),
        onToggleEditMode = {},
        onSaveGiftDetails = {},
        onNavigateBack = {},
        onPersonChange = {},
        onGiftChange = {},
        onPriceChange = {}
    )
}