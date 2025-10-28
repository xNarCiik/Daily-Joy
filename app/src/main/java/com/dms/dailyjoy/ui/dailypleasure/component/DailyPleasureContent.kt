package com.dms.dailyjoy.ui.dailypleasure.component

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.RenderMode
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
import com.dms.dailyjoy.ui.theme.dailyJoyGradients
import com.dms.dailyjoy.ui.util.LightDarkPreview
import com.dms.dailyjoy.ui.util.previewDailyPleasure
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

    // --- Vibrator
    val vibrator = remember(context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            manager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    // --- SoundPool
    val (soundPool, soundId) = remember(context) {
        val attrs = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        val sp = SoundPool.Builder().setMaxStreams(1).setAudioAttributes(attrs).build()
        val id = sp.load(context, R.raw.done, 1)
        sp to id
    }

    DisposableEffect(Unit) {
        onDispose { soundPool.release() }
    }

    // --- Lottie confetti
    val confettiComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.confetti))
    val confettiIsPlaying by rememberUpdatedState(showConfettiAnimation)
    val confettiProgress by animateLottieCompositionAsState(
        composition = confettiComposition,
        isPlaying = confettiIsPlaying,
        restartOnPlay = false,
        iterations = 1
    )

    LaunchedEffect(confettiIsPlaying) {
        if (confettiIsPlaying) {
            soundPool.setVolume(soundId, 0.25f, 0.25f)
            soundPool.play(soundId, 0.25f, 0.25f, 1, 0, 1f)

            if (vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(220L, VibrationEffect.DEFAULT_AMPLITUDE)
                    )
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(220L)
                }
            }
        }
    }
    LaunchedEffect(confettiProgress) {
        if (showConfettiAnimation && confettiProgress >= 1f) {
            showConfettiAnimation = false
        }
    }

    // --- Hint
    val (hintOffsetX, hintRotation) = swipeHintAnimation()

    // --- Swipe physics
    val animatedOffsetX = remember { Animatable(0f) }
    val animatedRotationZ = remember { Animatable(0f) }

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
            .padding(top = 12.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(visible = !uiState.isCardFlipped) {
            CurrentCategoryButton(
                category = uiState.selectedCategory,
                onClick = { showCategoryDialog = true }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                PleasureCard(
                    modifier = Modifier
                        .draggable(
                            orientation = Orientation.Horizontal,
                            enabled = uiState.isCardFlipped,
                            state = rememberDraggableState { delta ->
                                val friction = 0.22f
                                val next = animatedOffsetX.value + (delta * friction)
                                scope.launch {
                                    animatedOffsetX.snapTo(next.coerceIn(-1000f, 1000f))
                                    animatedRotationZ.snapTo(
                                        (animatedOffsetX.value / 24f).coerceIn(-5f, 15f)
                                    )
                                }
                            },
                            onDragStopped = { velocity ->
                                scope.launch {
                                    val threshold = 180f
                                    val offset = animatedOffsetX.value
                                    val v = velocity

                                    val shouldComplete =
                                        offset > threshold || (v > 2500f && offset > 0f)
                                    if (shouldComplete) {
                                        val target = 1200f
                                        launch {
                                            animatedOffsetX.animateTo(
                                                target,
                                                animationSpec = tween(
                                                    durationMillis = 520,
                                                    easing = LinearOutSlowInEasing
                                                )
                                            )
                                        }
                                        launch {
                                            animatedRotationZ.animateTo(
                                                15f, animationSpec = tween(520, easing = EaseInOut)
                                            )
                                        }
                                        delay(260)
                                        onEvent(DailyPleasureEvent.OnCardMarkedAsDone)
                                    } else {
                                        val stiffness = 500f
                                        val damping = 0.78f
                                        launch {
                                            animatedOffsetX.animateTo(
                                                0f,
                                                animationSpec = spring(
                                                    stiffness = stiffness,
                                                    dampingRatio = damping
                                                )
                                            )
                                        }
                                        launch {
                                            animatedRotationZ.animateTo(
                                                0f,
                                                animationSpec = spring(
                                                    stiffness = stiffness,
                                                    dampingRatio = damping
                                                )
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
                            alpha = 1f - (abs(animatedOffsetX.value) / 900f).coerceIn(0f, 1f)
                        },
                    pleasure = uiState.dailyPleasure,
                    flipped = uiState.isCardFlipped,
                    durationRotation = 1300,
                    onCardFlipped = {
                        showConfettiAnimation = true
                        onEvent(DailyPleasureEvent.OnCardFlipped)
                    },
                    onClick = { onEvent(DailyPleasureEvent.OnCardClicked) }
                )

                AnimatedVisibility(
                    visible = uiState.isCardFlipped,
                    enter = slideInVertically(
                        initialOffsetY = { it / 2 }
                    ) + fadeIn(),
                    exit = slideOutVertically(
                        targetOffsetY = { it / 2 }
                    ) + fadeOut()
                ) {
                    SwipeCompletionHint()
                }
            }
        }
    }

    // --- Overlay confetti
    if (showConfettiAnimation) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LottieAnimation(
                modifier = Modifier.size(320.dp),
                composition = confettiComposition,
                progress = { confettiProgress },
                renderMode = RenderMode.HARDWARE
            )
        }
    }
}

@Composable
private fun SwipeCompletionHint(modifier: Modifier = Modifier) {
    val infinite = androidx.compose.animation.core.rememberInfiniteTransition(label = "swipeCompletionHint")
    val arrowOffset by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "arrowOffset"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()

                .clip(RoundedCornerShape(24.dp))
                .background(Color.White.copy(alpha = 0.12f))
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Swipe une fois réalisé",//stringResource(R.string.swipe_to_complete),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Box {
                repeat(3) { index ->
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier
                            .size(28.dp)
                            .offset(x = (arrowOffset + (index * 8)).dp)
                            .graphicsLayer {
                                alpha = (1f - (index * 0.25f)) * 0.7f
                            },
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun swipeHintAnimation(): Pair<Float, Float> {
    val infinite = androidx.compose.animation.core.rememberInfiniteTransition(label = "swipeHint")
    val hintOffsetX by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3600
                0f at 0
                0f at 1200
                18f at 1700
                0f at 2400
                0f at 3600
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "hintOffset"
    )
    val hintRotation by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 3600
                0f at 0
                0f at 1200
                3f at 1700
                0f at 2400
                0f at 3600
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "hintRotation"
    )
    return hintOffsetX to hintRotation
}

@Composable
private fun CurrentCategoryButton(
    category: PleasureCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gradients = dailyJoyGradients()

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(gradients.background)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.White
                )
            }

            Text(
                text = stringResource(category.label),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint = Color.White.copy(alpha = 0.9f)
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
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = stringResource(R.string.daily_pleasure_choose_category),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.height(380.dp)
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
    val itemBackground = if (isSelected) {
        Brush.linearGradient(
            colors = listOf(
                category.iconTint.copy(alpha = 0.85f),
                category.iconTint.copy(alpha = 0.95f)
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                MaterialTheme.colorScheme.surfaceVariant,
                MaterialTheme.colorScheme.surfaceVariant
            )
        )
    }

    Box(
        modifier = modifier
            .height(110.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(itemBackground)
            .clickable(onClick = onClick)
            .padding(18.dp)
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
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected)
                                Color.White.copy(alpha = 0.25f)
                            else
                                category.iconTint.copy(alpha = 0.15f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = category.icon,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = if (isSelected) Color.White else category.iconTint
                    )
                }

                AnimatedVisibility(
                    visible = isSelected,
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
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
        Surface(color = MaterialTheme.colorScheme.background) {
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
}