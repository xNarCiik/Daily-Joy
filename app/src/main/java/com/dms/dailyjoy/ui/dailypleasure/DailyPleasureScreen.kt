package com.dms.dailyjoy.ui.dailypleasure

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dms.dailyjoy.R
import com.dms.dailyjoy.ui.DailyPleasureState
import com.dms.dailyjoy.ui.component.DailyPleasureCard
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview
import com.dms.dailyjoy.ui.util.previewDailyPleasureState
import com.dms.dailyjoy.ui.util.rotationCardAnimationDuration
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun DailyPleasureScreen(
    dailyPleasureState: DailyPleasureState,
    onCardFlipped: () -> Unit,
    onDraggingCard: (Boolean) -> Unit,
    onDonePleasure: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 12.dp, horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        if (dailyPleasureState.isLoading) {
            CircularProgressIndicator()
        } else if (dailyPleasureState.dailyPleasure.isDone) {
            DailyPleasureCompletedContent()
        } else {
            DailyPleasureContent(
                dailyPleasureState = dailyPleasureState,
                onCardFlipped = onCardFlipped,
                onDraggingCard = onDraggingCard,
                onDonePleasure = onDonePleasure
            )
        }
    }
}

@Composable
fun DailyPleasureContent(
    dailyPleasureState: DailyPleasureState,
    onCardFlipped: () -> Unit,
    onDraggingCard: (Boolean) -> Unit,
    onDonePleasure: () -> Unit
) {
    val isFlipped = dailyPleasureState.dailyPleasure.isFlipped
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

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))

        Text(
            text = dailyPleasureState.dailyMessage,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.weight(1f))

        val scope = rememberCoroutineScope()

        val animatedOffsetX = remember { Animatable(0f) }
        val animatedRotationZ = remember { Animatable(0f) }

        DailyPleasureCard(
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
                        onDraggingCard(true)
                        scope.launch {
                            animatedOffsetX.stop()
                            animatedRotationZ.stop()
                        }
                    },
                    onDragStopped = {
                        onDraggingCard(false)
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
                                onDonePleasure()
                            } else {
                                val springSpec = spring<Float>(dampingRatio = 0.7f)
                                launch { animatedOffsetX.animateTo(0f, springSpec) }
                                launch { animatedRotationZ.animateTo(0f, springSpec) }
                            }
                        }
                    }
                )
                .offset(x = animatedOffsetX.value.dp)
                .graphicsLayer {
                    rotationZ = animatedRotationZ.value
                    cameraDistance = 8 * density
                    alpha = 1f - (abs(animatedOffsetX.value) / 800f).coerceIn(0f, 1f)
                },
            pleasure = dailyPleasureState.dailyPleasure,
            durationRotation = rotationCardAnimationDuration,
            onCardFlipped = {
                showConfettiAnimation = true
                onCardFlipped()
            }
        )

        Spacer(Modifier.weight(1f))

        InfoText(cardIsFlipped = isFlipped)

        Spacer(Modifier.weight(1f))
    }

    // Animation Confetti
    val confettiComposition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(
            resId = R.raw.confetti
        )
    )
    val confettiProgress by animateLottieCompositionAsState(
        composition = confettiComposition,
        isPlaying = showConfettiAnimation,
        restartOnPlay = false
    )

    if (showConfettiAnimation) {
        LottieAnimation(
            composition = confettiComposition,
            progress = { confettiProgress }
        )
    }
}

@Composable
private fun DailyPleasureCompletedContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val successComposition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(resId = R.raw.checkmark)
        )
        val successProgress by animateLottieCompositionAsState(
            composition = successComposition,
            isPlaying = true,
            restartOnPlay = false
        )

        LottieAnimation(
            modifier = Modifier.size(200.dp),
            composition = successComposition,
            progress = { successProgress }
        )

        Text(
            text = "Graine de joie plantée !",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Super ! Reviens demain pour découvrir ton prochain plaisir.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp, start = 32.dp, end = 32.dp)
        )
    }
}

@LightDarkPreview
@Composable
fun DailyPleasureNotCompletedScreenPreview() {
    DailyJoyTheme {
        DailyPleasureContent(
            dailyPleasureState = previewDailyPleasureState.copy(
                dailyPleasure = previewDailyPleasureState.dailyPleasure.copy(isDone = false)
            ),
            onCardFlipped = {},
            onDraggingCard = {},
            onDonePleasure = {}
        )
    }
}

@LightDarkPreview
@Composable
fun DailyPleasureCompletedScreenPreview() {
    DailyJoyTheme {
        DailyPleasureContent(
            dailyPleasureState = previewDailyPleasureState.copy(
                dailyPleasure = previewDailyPleasureState.dailyPleasure.copy(isDone = true)
            ),
            onCardFlipped = {},
            onDraggingCard = {},
            onDonePleasure = {}
        )
    }
}
