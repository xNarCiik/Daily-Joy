package com.dms.dailyjoy.ui.dailypleasure.component

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dms.dailyjoy.R
import com.dms.dailyjoy.data.model.PleasureCategory
import com.dms.dailyjoy.ui.component.PleasureCard
import com.dms.dailyjoy.ui.dailypleasure.DailyPleasureEvent
import com.dms.dailyjoy.ui.dailypleasure.DailyPleasureScreenState
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview
import com.dms.dailyjoy.ui.util.previewDailyPleasure
import com.dms.dailyjoy.ui.util.rotationCardAnimationDuration
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun DailyPleasureContent(
    uiState: DailyPleasureScreenState.Ready,
    onEvent: (DailyPleasureEvent) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var showConfettiAnimation by remember { mutableStateOf(false) }
    var showCategoryDialog by remember { mutableStateOf(false) }

    // --- Vibrator setup ---
    val vibrator = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            manager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    // --- Lottie confetti ---
    val confettiComposition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.confetti)
    )
    val confettiProgress by animateLottieCompositionAsState(
        composition = confettiComposition,
        isPlaying = showConfettiAnimation,
        restartOnPlay = false
    )

    // --- Play sound & vibration when confetti starts ---
    LaunchedEffect(showConfettiAnimation) {
        if (showConfettiAnimation) {
            // Sound
            try {
                MediaPlayer.create(context, R.raw.done).apply {
                    setOnCompletionListener { it.release() }
                    setVolume(0.25f, 0.25f)
                    start()
                }
            } catch (_: Exception) {
            }

            // Vibration
            if (vibrator.hasVibrator()) {
                val duration = 400L
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
                    )
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(duration)
                }
            }
        }
    }

    // --- Hint animation ---
    val (hintOffsetX, hintRotation) = swipeHintAnimation()

    // --- Swipe animations ---
    val animatedOffsetX = remember { Animatable(0f) }
    val animatedRotationZ = remember { Animatable(0f) }

    // --- Dialog categories ---
    if (showCategoryDialog) {
        CategorySelectionDialog(
            categories = uiState.availableCategories,
            selectedCategory = uiState.selectedCategory,
            onCategorySelected = {
                onEvent(DailyPleasureEvent.OnCategorySelected(it))
                showCategoryDialog = false
            },
            onDismiss = { showCategoryDialog = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CurrentCategoryButton(
            category = uiState.selectedCategory,
            onClick = { showCategoryDialog = true }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- carte + confetti ---
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            // Carte
            PleasureCard(
                modifier = Modifier
                    .draggable(
                        orientation = Orientation.Horizontal,
                        enabled = uiState.isCardFlipped,
                        state = rememberDraggableState { delta ->
                            scope.launch {
                                val friction = 0.25f
                                animatedOffsetX.snapTo(animatedOffsetX.value + delta * friction)
                                animatedRotationZ.snapTo(
                                    (animatedOffsetX.value / 20f).coerceIn(
                                        -5f,
                                        15f
                                    )
                                )
                            }
                        },
                        onDragStopped = {
                            scope.launch {
                                val threshold = 180f
                                val offset = animatedOffsetX.value

                                if (offset > threshold) {
                                    launch {
                                        animatedOffsetX.animateTo(
                                            1000f, tween(600, easing = LinearOutSlowInEasing)
                                        )
                                    }
                                    launch { animatedRotationZ.animateTo(15f, tween(600)) }
                                    delay(300)
                                    onEvent(DailyPleasureEvent.OnCardMarkedAsDone)
                                } else {
                                    launch {
                                        animatedOffsetX.animateTo(
                                            0f,
                                            spring(dampingRatio = 0.7f)
                                        )
                                    }
                                    launch {
                                        animatedRotationZ.animateTo(
                                            0f,
                                            spring(dampingRatio = 0.7f)
                                        )
                                    }
                                }
                            }
                        }
                    )
                    .offset(x = (animatedOffsetX.value + if (uiState.isCardFlipped) hintOffsetX else 0f).dp)
                    .graphicsLayer {
                        rotationZ =
                            animatedRotationZ.value + if (uiState.isCardFlipped) hintRotation else 0f
                        alpha = 1f - (abs(animatedOffsetX.value) / 800f).coerceIn(0f, 1f)
                        cameraDistance = 8 * density
                    },
                pleasure = uiState.dailyPleasure,
                flipped = uiState.isCardFlipped,
                durationRotation = rotationCardAnimationDuration,
                onCardFlipped = {
                    showConfettiAnimation = true
                    onEvent(DailyPleasureEvent.OnCardFlipped)
                },
                onClick = { onEvent(DailyPleasureEvent.OnCardClicked) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (!uiState.isCardFlipped)
                    stringResource(R.string.pleasure_tap_card_text)
                else
                    stringResource(R.string.pleasure_swip_card_text),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    if (showConfettiAnimation) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LottieAnimation(
                modifier = Modifier
                    .size(350.dp),
                composition = confettiComposition,
                progress = { confettiProgress },
            )
        }
    }
}

@Composable
fun swipeHintAnimation(): Pair<Float, Float> {
    val infiniteTransition = rememberInfiniteTransition(label = "swipeHint")

    val hintOffsetX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                0f at 0
                0f at 1000
                20f at 1500
                0f at 2000
                0f at 3000
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "hintOffset"
    )

    val hintRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3000
                0f at 0
                0f at 1000
                3f at 1500
                0f at 2000
                0f at 3000
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "hintRotation"
    )

    return Pair(hintOffsetX, hintRotation)
}


@Composable
private fun CurrentCategoryButton(
    category: PleasureCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = stringResource(category.label),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun CategorySelectionDialog(
    categories: List<PleasureCategory>,
    selectedCategory: PleasureCategory?,
    onCategorySelected: (PleasureCategory) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.daily_pleasure_choose_category),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.height(320.dp)
                ) {
                    items(categories, key = { it }) { category ->
                        CategoryDialogItem(
                            category = category,
                            isSelected = category == selectedCategory,
                            onClick = { onCategorySelected(category) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryDialogItem(
    category: PleasureCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(100.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected) {
                    Brush.verticalGradient(category.gradientColors)
                } else {
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.surfaceContainerHigh,
                            MaterialTheme.colorScheme.surfaceContainerHigh
                        )
                    )
                }
            )
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                )

                AnimatedVisibility(
                    visible = isSelected,
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                    }
                }
            }

            Text(
                text = stringResource(category.label),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )
        }
    }
}

@LightDarkPreview
@Composable
private fun DailyPleasureContentPreview() {
    DailyJoyTheme {
        DailyPleasureContent(
            uiState = DailyPleasureScreenState.Ready(
                availableCategories = PleasureCategory.entries,
                dailyPleasure = previewDailyPleasure,
                isCardFlipped = true,
                selectedCategory = PleasureCategory.entries.first()
            ),
            onEvent = {}
        )
    }
}
