package com.dms.dailyjoy.ui.dailypleasure.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dms.dailyjoy.R
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SetupContent(
    modifier: Modifier = Modifier,
    currentPleasureCount: Int,
    requiredCount: Int = 7,
    onConfigureClick: () -> Unit,
) {
    // Animations d'entrée
    val illustrationScale = remember { Animatable(0.7f) }
    val illustrationAlpha = remember { Animatable(0f) }
    val titleAlpha = remember { Animatable(0f) }
    val subtitleAlpha = remember { Animatable(0f) }
    val buttonScale = remember { Animatable(0.8f) }
    val buttonAlpha = remember { Animatable(0f) }
    val progressAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Animation séquentielle
        launch {
            illustrationAlpha.animateTo(1f, tween(400, easing = FastOutSlowInEasing))
        }
        launch {
            illustrationScale.animateTo(1f, tween(400, easing = FastOutSlowInEasing))
        }
        delay(100)
        launch {
            titleAlpha.animateTo(1f, tween(300))
        }
        delay(100)
        launch {
            subtitleAlpha.animateTo(1f, tween(300))
        }
        delay(200)
        launch {
            progressAlpha.animateTo(1f, tween(300))
        }
        delay(100)
        launch {
            buttonAlpha.animateTo(1f, tween(300))
        }
        launch {
            buttonScale.animateTo(
                1f,
                spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
                        )
                    )
                )
                .padding(vertical = 48.dp, horizontal = 24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val composition = rememberLottieComposition(
                    spec = LottieCompositionSpec.RawRes(R.raw.plant_growing)
                )
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .scale(illustrationScale.value)
                        .alpha(illustrationAlpha.value)
                ) {
                    LottieAnimation(
                        composition = composition.value,
                        iterations = LottieConstants.IterateForever
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.setup_title),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.alpha(titleAlpha.value)
                )

                Text(
                    text = stringResource(
                        R.string.setup_subtitle,
                        currentPleasureCount,
                        requiredCount
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.alpha(subtitleAlpha.value)
                )

                Spacer(modifier = Modifier.height(8.dp))

                ProgressIndicator(
                    current = currentPleasureCount,
                    required = requiredCount,
                    modifier = Modifier.alpha(progressAlpha.value)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Bouton CTA
                Button(
                    onClick = onConfigureClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(buttonScale.value)
                        .alpha(buttonAlpha.value),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Text(
                        text = stringResource(R.string.setup_button_text),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProgressIndicator(
    current: Int,
    required: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(required) { index ->
            val isCompleted = index < current
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(
                        if (isCompleted) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        }
                    )
            )
        }
    }
}

@LightDarkPreview
@Composable
private fun SetupContentPreview() {
    DailyJoyTheme {
        Surface {
            SetupContent(
                currentPleasureCount = 4,
                requiredCount = 7,
                onConfigureClick = {}
            )
        }
    }
}
