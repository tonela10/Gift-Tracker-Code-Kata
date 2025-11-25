package com.sedilant.gifttracker.ui.giftDetail

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun GiftDetailScreen(
    itemId: String?,
    viewModel: GiftDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Regalo", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    // Animación para el icono de navegación (Atrás/Cerrar)
                    Crossfade(
                        targetState = uiState.isEditMode,
                        label = "nav-icon-animation"
                    ) { isEditMode ->
                        if (isEditMode) {
                            IconButton(onClick = { viewModel.toggleEditMode() }) {
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
            // Animación para el botón inferior (Editar/Guardar)
            Crossfade(
                targetState = uiState.isEditMode,
                label = "bottom-bar-animation"
            ) { isEditMode ->
                val buttonColor = if (isEditMode) Color(0xFF4CAF50) else Color(0xFFC62828)
                val buttonText = if (isEditMode) "Guardar" else "Editar"
                val onClickAction = if (isEditMode) {
                    { viewModel.saveGiftDetails() }
                } else {
                    { viewModel.toggleEditMode() }
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
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DetailItemCard(label = "Persona", value = uiState.person)
            DetailItemCard(label = "Regalo", value = uiState.gift)
            DetailItemCard(label = "Precio", value = "€${uiState.price}")
            StatusItemCard(isPurchased = uiState.isPurchased)
        }
    }
}

@Composable
private fun DetailItemCard(label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun StatusItemCard(isPurchased: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Estado", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Comprado",
                    tint = Color(0xFF4CAF50)
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = if (isPurchased) "Comprado" else "Pendiente",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}


@Preview(showBackground = true, name = "View Mode")
@Composable
fun GiftDetailScreenPreview() {
    MaterialTheme {
        GiftDetailScreen(
            itemId = "123",
            onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Edit Mode")
@Composable
fun GiftDetailScreenEditModePreview() {
    // Mock ViewModel for previewing edit mode

    MaterialTheme {
        GiftDetailScreen(
            itemId = "123",
            onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, name = "New Gift Mode")
@Composable
fun GiftDetailScreenNewGiftPreview() {
    // Mock ViewModel for previewing a new gift (starts in edit mode)

    MaterialTheme {
        GiftDetailScreen(
            itemId = null, // itemId is null for a new gift
            onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailItemCardPreview() {
    MaterialTheme {
        DetailItemCard(label = "Persona", value = "Carlos")
    }
}

@Preview(showBackground = true)
@Composable
private fun StatusItemCardPreview() {
    MaterialTheme {
        StatusItemCard(isPurchased = true)
    }
}
