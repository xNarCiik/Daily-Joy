package com.dms.dailyjoy.ui.dailypleasure.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.ui.util.alphaInfoTextAnimationDuration
import com.dms.dailyjoy.ui.util.xOffsetInfoTextAnimationDuration

@Composable
fun InfoText(modifier: Modifier = Modifier, cardIsFlipped: Boolean) {
    val pulseTransition = rememberInfiniteTransition(label = "pulseTransition")
    val swipeNudgeTransition = rememberInfiniteTransition(label = "swipeNudgeTransition")

    val alpha by if (!cardIsFlipped) pulseTransition.animateFloat(
        initialValue = 0.75f,
        targetValue = 0.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = alphaInfoTextAnimationDuration),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    ) else remember { mutableFloatStateOf(1f) }

    val xOffset by if (cardIsFlipped) swipeNudgeTransition.animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = xOffsetInfoTextAnimationDuration),
            repeatMode = RepeatMode.Reverse
        ),
        label = "swipeNudgeOffset"
    ) else remember { mutableFloatStateOf(0f) }

    val bottomText =
        if (cardIsFlipped) "Swipe à droite une fois réalisée :)" else "Clique sur la carte pour le découvrir..."
    Text(
        modifier = modifier
            .alpha(alpha)
            .offset(x = xOffset.dp),
        text = bottomText,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center
    )
}