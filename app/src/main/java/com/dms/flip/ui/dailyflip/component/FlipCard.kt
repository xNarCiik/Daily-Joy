package com.dms.flip.ui.dailyflip.component

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.unit.sp
import com.dms.flip.R
import com.dms.flip.domain.model.Pleasure
import com.dms.flip.ui.theme.FlipTheme
import com.dms.flip.ui.theme.flipGradients
import com.dms.flip.ui.util.LightDarkPreview
import com.dms.flip.ui.util.previewDailyPleasure
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PleasureCard(
    modifier: Modifier = Modifier,
    pleasure: Pleasure?,
    flipped: Boolean,
    durationRotation: Int,
    onCardFlipped: () -> Unit,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isFlipped by remember { mutableStateOf(flipped) }
    var isJumping by remember { mutableStateOf(false) }
    var shouldAnimate by remember { mutableStateOf(false) }

    var swipeOffset by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(flipped) {
        if (isFlipped != flipped) {
            shouldAnimate = false
            isFlipped = flipped
        }
    }

    LaunchedEffect(pleasure) {
        if (pleasure != null) {
            if (!isFlipped && !isJumping) {
                scope.launch {
                    isJumping = true
                    delay(250)
                    shouldAnimate = true
                    isFlipped = true
                    delay(100)
                    isJumping = false
                }
            }
        }
    }

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = if (shouldAnimate) {
            tween(
                durationMillis = durationRotation,
                easing = { it * it * (3 - 2 * it) }
            )
        } else {
            tween(durationMillis = 0)
        },
        label = "rotationAnimation",
        finishedListener = { finalValue ->
            if (finalValue == 180f && !flipped) {
                onCardFlipped()
                playFlipSoundAndVibration(context)
            }
            shouldAnimate = false
        }
    )

    val jumpScale by animateFloatAsState(
        targetValue = if (isJumping) 1.05f else 1f,
        animationSpec = tween(2000, easing = { overshootEasing(t = it) }),
        label = "scaleAnimation"
    )

    val jumpOffset by animateFloatAsState(
        targetValue = if (isJumping) (-6f) else 0f,
        animationSpec = tween(2000, easing = { overshootEasing(t = it) }),
        label = "offsetAnimation"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8 * density
                scaleX = jumpScale
                scaleY = jumpScale
                translationY = jumpOffset
                translationX = swipeOffset
            },
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = onClick
    ) {
        if (rotation < 90f) {
            BackCard()
        } else {
            pleasure?.let {
                CardContent(
                    modifier = Modifier.graphicsLayer { rotationY = 180f },
                    pleasure = it
                )
            }
        }
    }
}

private fun playFlipSoundAndVibration(context: Context) {
    try {
        // 📳 VIBRATION
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }

        vibrator?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                it.vibrate(
                    VibrationEffect.createOneShot(
                        150,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                @Suppress("DEPRECATION")
                it.vibrate(150)
            }
        }


        val mediaPlayer = MediaPlayer.create(context, R.raw.done)
        mediaPlayer?.apply {
            setOnCompletionListener { release() }
            start()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun overshootEasing(t: Float, tension: Float = 2.5f) =
    (t - 1).let { it * it * ((tension + 1) * it + tension) + 1 }

@Composable
private fun BackCard() {
    val gradients = flipGradients()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradients.card)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            repeat(5) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(4) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.06f))
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.3f),
                                Color.White.copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.TouchApp,
                        contentDescription = null,
                        modifier = Modifier.size(44.dp),
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = stringResource(R.string.pleasure_card_back_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 34.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.pleasure_card_back_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.95f),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
private fun CardContent(
    modifier: Modifier = Modifier,
    pleasure: Pleasure
) {
    val gradients = flipGradients()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(gradients.card)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(50.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            ) {
                Text(
                    text = stringResource(pleasure.category.label),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Text(
                text = pleasure.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 32.sp
            )

            Text(
                text = pleasure.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 24.sp
            )
        }
    }
}

@LightDarkPreview
@Composable
private fun NotFlippedCardPreview() {
    FlipTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            PleasureCard(
                pleasure = previewDailyPleasure,
                durationRotation = 0,
                flipped = false,
                onCardFlipped = {},
                onClick = {}
            )
        }
    }
}

@LightDarkPreview
@Composable
private fun FlippedCardPreview() {
    FlipTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            PleasureCard(
                pleasure = previewDailyPleasure,
                durationRotation = 0,
                flipped = true,
                onCardFlipped = {},
                onClick = {}
            )
        }
    }
}
