package com.sedilant.gifttracker.ui.giftDetail

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Euro
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sedilant.gifttracker.R
import com.sedilant.gifttracker.ui.theme.BackgroundLight

@Composable
public fun GiftDetailScreen(
    itemId: Long?,
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
            DetailsTopBar(
                isEditMode = uiState.isEditMode,
                onToggleEditMode = onToggleEditMode,
                onNavigateBack = onNavigateBack
            )
        },
        bottomBar = {
            DetailsBottomBar(
                isEditMode = uiState.isEditMode,
                onToggleEditMode = onToggleEditMode,
                onSaveGiftDetails = {
                    onSaveGiftDetails()
                }
            )
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
                label = stringResource(id = R.string.person_label),
                value = uiState.person,
                isEditMode = uiState.isEditMode,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
                onValueChange = { onPersonChange(it) }
            )
            DetailItemCard(
                label = stringResource(id = R.string.gift_label),
                value = uiState.gift,
                isEditMode = uiState.isEditMode,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
                onValueChange = { onGiftChange(it) }
            )
            DetailItemCard(
                label = stringResource(id = R.string.price_label),
                value = uiState.price,
                isEditMode = uiState.isEditMode,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                onValueChange = { onPriceChange(it) },
                trailingIcon = { Icon(imageVector = Icons.Rounded.Euro, contentDescription = null) }
            )
            StatusItemCard(isPurchased = uiState.isPurchased)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsTopBar(
    isEditMode: Boolean,
    onToggleEditMode: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Surface(modifier = Modifier.background(BackgroundLight)) {
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = if (!isEditMode) stringResource(id = R.string.gift_detail_title) else stringResource(
                            id = R.string.editing
                        ),
                        fontWeight = FontWeight.Bold
                    )
                    if (isEditMode) {
                        Spacer(modifier = Modifier.width(8.dp))
                        DotAnimation()
                    }
                }
            },
            navigationIcon = {
                Crossfade(
                    targetState = isEditMode,
                    label = "nav-icon-animation"
                ) { isEditMode ->
                    if (isEditMode) {
                        IconButton(onClick = { onToggleEditMode() }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(id = R.string.exit_edit_mode_content_description)
                            )
                        }
                    } else {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = stringResource(id = R.string.back_content_description)
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun DetailsBottomBar(
    isEditMode: Boolean,
    onToggleEditMode: () -> Unit,
    onSaveGiftDetails: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isEditMode) 1.05f else 1f,
        label = "button-scale"
    )

    Crossfade(
        targetState = isEditMode,
        label = "bottom-bar-animation"
    ) { isEditMode ->
        val buttonColor = if (isEditMode) Color(0xFF4CAF50) else Color(0xFFC62828)
        val buttonText =
            if (isEditMode) stringResource(id = R.string.save_button) else stringResource(id = R.string.edit_button)
        val onClickAction = if (isEditMode) {
            { onSaveGiftDetails() }
        } else {
            { onToggleEditMode() }
        }


        Button(
            onClick = onClickAction,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
            shape = RoundedCornerShape(50),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 8.dp
            ),
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

@Composable
fun DotAnimation() {
    val infiniteTransition = rememberInfiniteTransition()

    @Composable
    fun Dot(alpha: Float) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .graphicsLayer {
                    this.alpha = alpha
                }
                .background(
                    color = Color.Black,
                    shape = CircleShape
                )
        )
    }

    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        val alphas = (0..2).map {
            infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 1000,
                        delayMillis = 200,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Restart
                )
            )
        }
        alphas.forEach {
            Dot(alpha = it.value)
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