package com.dms.dailyjoy.ui.dailypleasure.component

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dms.dailyjoy.R
import com.dms.dailyjoy.data.model.Pleasure
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
    val isFlipped = uiState.isCardFlipped
    val isDone = uiState.drawnPleasure?.isDone ?: false
    var showConfettiAnimation by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val vibrator = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    LaunchedEffect(showConfettiAnimation) {
        if (showConfettiAnimation) {
            try {
                MediaPlayer.create(context, R.raw.done).apply {
                    setOnCompletionListener { it.release() }
                    setVolume(0.25f, 0.25f)
                    start()
                }
            } catch (_: Exception) {
            }

            if (vibrator.hasVibrator()) {
                val duration = 400L
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val vibrationEffect = VibrationEffect.createOneShot(
                        duration,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                    vibrator.vibrate(vibrationEffect)
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(duration)
                }
            }
        }
    }

    // Animation de hint pour swipe (uniquement si flipped et non done)
    val infiniteTransition = rememberInfiniteTransition(label = "swipeHint")
    val hintOffsetX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isFlipped && !isDone) 40f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = { t ->
                when {
                    t < 0.2f -> 0f
                    t < 0.5f -> (t - 0.2f) / 0.3f
                    t < 0.7f -> 1f - (t - 0.5f) / 0.2f
                    else -> 0f
                }
            }),
            repeatMode = RepeatMode.Restart
        ),
        label = "hintOffset"
    )

    val hintRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isFlipped && !isDone) 3f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = { t ->
                when {
                    t < 0.2f -> 0f
                    t < 0.5f -> (t - 0.2f) / 0.3f
                    t < 0.7f -> 1f - (t - 0.5f) / 0.2f
                    else -> 0f
                }
            }),
            repeatMode = RepeatMode.Restart
        ),
        label = "hintRotation"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        CategorySelector(
            categories = uiState.availableCategories,
            selectedCategory = uiState.selectedCategory,
            onCategorySelected = { category ->
                onEvent(DailyPleasureEvent.OnCategorySelected(category))
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        val scope = rememberCoroutineScope()

        val animatedOffsetX = remember { Animatable(0f) }
        val animatedRotationZ = remember { Animatable(0f) }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            // Glow effect (visible seulement quand la carte est prête)
            if (!isFlipped || isDone) {
                CardGlowEffect()
            }

            PleasureCard(
                modifier = Modifier
                    .draggable(
                        orientation = Orientation.Horizontal,
                        enabled = isFlipped,
                        state = rememberDraggableState { delta ->
                            scope.launch {
                                val friction = if (delta < 0) 0.2f else 0.5f
                                val newX = animatedOffsetX.value + (delta * friction)
                                animatedOffsetX.snapTo(newX)
                                val newRot = (newX / 18f).coerceIn(-5f, 20f)
                                animatedRotationZ.snapTo(newRot)
                            }
                        },
                        onDragStarted = {
                            scope.launch {
                                animatedOffsetX.stop()
                                animatedRotationZ.stop()
                            }
                        },
                        onDragStopped = {
                            scope.launch {
                                val swipeThreshold = 150f
                                if (animatedOffsetX.value > swipeThreshold) {
                                    val animSpec = tween<Float>(
                                        durationMillis = 3000,
                                        easing = LinearOutSlowInEasing
                                    )
                                    launch { animatedOffsetX.animateTo(1000f, animSpec) }
                                    launch { animatedRotationZ.animateTo(20f, animSpec) }

                                    delay(400)

                                    onEvent(DailyPleasureEvent.OnCardMarkedAsDone)
                                } else {
                                    val springSpec = spring<Float>(dampingRatio = 0.7f)
                                    launch { animatedOffsetX.animateTo(0f, springSpec) }
                                    launch { animatedRotationZ.animateTo(0f, springSpec) }
                                }
                            }
                        }
                    )
                    .offset(x = (animatedOffsetX.value + hintOffsetX).dp)
                    .graphicsLayer {
                        rotationZ = animatedRotationZ.value + hintRotation
                        cameraDistance = 8 * density
                        alpha = 1f - (abs(animatedOffsetX.value) / 800f).coerceIn(0f, 1f)
                    },
                pleasure = uiState.drawnPleasure ?: Pleasure(),
                flipped = uiState.isCardFlipped,
                durationRotation = rotationCardAnimationDuration,
                onCardFlipped = {
                    showConfettiAnimation = true
                    onEvent(DailyPleasureEvent.OnCardFlipped)
                }
            )
        }

        Text(
            text = when {
                !isFlipped -> stringResource(R.string.pleasure_tap_card_text)
                isDone -> ""
                else -> stringResource(R.string.pleasure_swip_card_text)
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }

    // Animation Confetti
    val confettiComposition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(resId = R.raw.confetti)
    )
    val confettiProgress by animateLottieCompositionAsState(
        composition = confettiComposition,
        isPlaying = showConfettiAnimation,
        restartOnPlay = false
    )

    if (showConfettiAnimation) {
        Box(modifier = Modifier.fillMaxSize()) {
            LottieAnimation(
                composition = confettiComposition,
                progress = { confettiProgress }
            )
        }
    }
}

@Composable
private fun CardGlowEffect() {
    // Animation de pulse pour le glow
    val infiniteTransition = rememberInfiniteTransition(label = "glowPulse")
    val glowScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowScale"
    )

    // TODO: Implémenter le vrai glow effect avec Canvas ou Modifier.drawBehind
    // Pour l'instant, placeholder simple
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(280.dp)
            .graphicsLayer {
                scaleX = glowScale
                scaleY = glowScale
                alpha = 0.3f
            }
        // TODO: Ajouter le blur et le gradient radial
    )
}

@LightDarkPreview
@Composable
private fun DailyPleasureContentPreview() {
    DailyJoyTheme {
        DailyPleasureContent(
            uiState = DailyPleasureScreenState.Ready(
                availableCategories = PleasureCategory.entries,
                drawnPleasure = previewDailyPleasure,
                isCardFlipped = true
            ),
            onEvent = {}
        )
    }
}
