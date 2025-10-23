package com.dms.dailyjoy.ui.dailypleasure

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
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dms.dailyjoy.R
import com.dms.dailyjoy.ui.component.AppHeader
import com.dms.dailyjoy.ui.component.PleasureCard
import com.dms.dailyjoy.ui.settings.manage.component.LoadingState
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview
import com.dms.dailyjoy.ui.util.previewDailyPleasureUiState
import com.dms.dailyjoy.ui.util.rotationCardAnimationDuration
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun DailyPleasureScreen(
    modifier: Modifier = Modifier,
    uiState: DailyPleasureUiState,
    onEvent: (DailyPleasureEvent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        AppHeader(
            title = stringResource(R.string.daily_pleasure_title),
            subtitle = uiState.headerMessage,
            icon = Icons.Default.EmojiEvents
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> {
                    LoadingState()
                }

                uiState.dailyPleasure.isDone -> {
                    DailyPleasureCompletedContent()
                }

                else -> {
                    DailyPleasureContent(
                        uiState = uiState,
                        onEvent = onEvent
                    )
                }
            }
        }
    }
}

@Composable
fun DailyPleasureContent(
    uiState: DailyPleasureUiState,
    onEvent: (DailyPleasureEvent) -> Unit
) {
    val isFlipped = uiState.dailyPleasure.isFlipped
    val isDone = uiState.dailyPleasure.isDone
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
                // Easing personnalisé : pause au début et à la fin
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
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        val scope = rememberCoroutineScope()

        val animatedOffsetX = remember { Animatable(0f) }
        val animatedRotationZ = remember { Animatable(0f) }

        Box(contentAlignment = Alignment.Center) {
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
                pleasure = uiState.dailyPleasure,
                durationRotation = rotationCardAnimationDuration,
                onCardFlipped = {
                    showConfettiAnimation = true
                    onEvent(DailyPleasureEvent.OnCardFlipped)
                }
            )
        }
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
private fun DailyPleasureCompletedContent() {
    var playAnimation by rememberSaveable { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Success Message Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                        )
                    )
                )
                .padding(vertical = 48.dp, horizontal = 24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.pleasure_completed_title),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Success Animation Container
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    val successComposition by rememberLottieComposition(
                        spec = LottieCompositionSpec.RawRes(resId = R.raw.checkmark)
                    )
                    val successProgress by animateLottieCompositionAsState(
                        composition = successComposition,
                        isPlaying = playAnimation,
                        restartOnPlay = false
                    )

                    LaunchedEffect(successProgress) {
                        if (successProgress == 1f) {
                            playAnimation = false
                        }
                    }

                    LottieAnimation(
                        modifier = Modifier.size(140.dp),
                        composition = successComposition,
                        progress = { if (playAnimation) successProgress else 1.0f }
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(id = R.string.pleasure_completed_subtitle),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@LightDarkPreview
@Composable
fun DailyPleasureNotCompletedScreenPreview() {
    DailyJoyTheme {
        Surface {
            DailyPleasureScreen(
                uiState = previewDailyPleasureUiState,
                onEvent = {}
            )
        }
    }
}

@LightDarkPreview
@Composable
fun DailyPleasureCompletedScreenPreview() {
    DailyJoyTheme {
        Surface {
            DailyPleasureScreen(
                uiState = previewDailyPleasureUiState.copy(
                    dailyPleasure = previewDailyPleasureUiState.dailyPleasure.copy(isDone = true)
                ),
                onEvent = {}
            )
        }
    }
}

@LightDarkPreview
@Composable
fun DailyPleasureFlippedScreenPreview() {
    DailyJoyTheme {
        Surface {
            DailyPleasureScreen(
                uiState = previewDailyPleasureUiState.copy(
                    dailyPleasure = previewDailyPleasureUiState.dailyPleasure.copy(
                        isFlipped = true
                    )
                ),
                onEvent = {}
            )
        }
    }
}
