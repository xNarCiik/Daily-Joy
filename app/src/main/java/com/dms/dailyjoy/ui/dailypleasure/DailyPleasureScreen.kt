package com.dms.dailyjoy.ui.dailypleasure

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dms.dailyjoy.R
import com.dms.dailyjoy.ui.DailyPleasureState
import com.dms.dailyjoy.ui.PleasureViewModel
import com.dms.dailyjoy.ui.component.DailyPleasureCard
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview
import com.dms.dailyjoy.ui.util.previewDailyPleasureState
import com.dms.dailyjoy.ui.util.rotationCardAnimationDuration
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun DailyPleasureScreen() {
    val viewModel: PleasureViewModel = hiltViewModel()
    val dailyPleasureState by viewModel.state.collectAsState()

    DailyPleasureContent(
        dailyPleasureState = dailyPleasureState,
        onCardFlipped = { viewModel.onDailyCardFlipped() },
        onDonePleasure = { viewModel.markDailyCardAsDone() }
    )
}

@Composable
private fun DailyPleasureContent(
    dailyPleasureState: DailyPleasureState,
    onCardFlipped: () -> Unit,
    onDonePleasure: () -> Unit
) {
    val isFlipped = dailyPleasureState.dailyPleasure.isFlipped
    var showConfettiAnimation by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 12.dp, horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
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
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = {
                                scope.launch { animatedOffsetX.stop() }
                                scope.launch { animatedRotationZ.stop() }
                            },
                            onDragEnd = {
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
                                        val springSpec = spring<Float>(
                                            dampingRatio = 0.7f,
                                        )
                                        launch { animatedOffsetX.animateTo(0f, springSpec) }
                                        launch { animatedRotationZ.animateTo(0f, springSpec) }
                                    }
                                }
                            },
                            onDrag = { change, dragAmount ->
                                change.consume() // Use to block pager
                                if (isFlipped) {
                                    scope.launch {
                                        val friction = 0.5f
                                        var newX = animatedOffsetX.value + (dragAmount.x * friction)

                                        if (dragAmount.x < 0) {
                                            val resistance = 0.2f
                                            newX =
                                                animatedOffsetX.value + (dragAmount.x * resistance)
                                        }

                                        animatedOffsetX.snapTo(newX)
                                        val newRot = (newX / 18f).coerceIn(-5f, 20f)
                                        animatedRotationZ.snapTo(newRot)
                                    }
                                }
                            }
                        )
                    }
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
}

@LightDarkPreview
@Composable
fun DailyPleasureContentPreview() {
    DailyJoyTheme {
        DailyPleasureContent(
            dailyPleasureState = previewDailyPleasureState,
            onCardFlipped = {},
            onDonePleasure = {}
        )
    }
}