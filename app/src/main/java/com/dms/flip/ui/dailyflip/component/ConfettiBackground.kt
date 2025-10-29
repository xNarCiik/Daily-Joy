package com.dms.flip.ui.dailyflip.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

data class Confetto(
    val id: Int,
    val color: Color,
    val size: Float,
    val startX: Float,
    val startY: Float,
    val duration: Int,
    val delay: Int
)

@Composable
fun ConfettiBackground(
    modifier: Modifier = Modifier,
    isVisible: Boolean = true
) {
    if (!isVisible) return

    val confetti = remember {
        listOf(
            // Confettis violets/lavande
            Confetto(1, Color(0xFFD0BCFF), 8f, 0.2f, 0.1f, 3000, 0),
            Confetto(4, Color(0xFFD0BCFF), 10f, 0.1f, 0.4f, 3800, 300),

            // Confettis cyan/bleu
            Confetto(2, Color(0xFF66DFD9), 12f, 0.85f, 0.2f, 3500, 200),
            Confetto(5, Color(0xFF66DFD9), 8f, 0.75f, 0.35f, 3200, 500),

            // Confettis corail/pêche
            Confetto(3, Color(0xFFFFB5A7), 8f, 0.4f, 0.05f, 4000, 100),
            Confetto(6, Color(0xFFFFB5A7), 8f, 0.05f, 0.5f, 3600, 400),

            // Confettis jaunes (ajout pour plus de variété)
            Confetto(7, Color(0xFFFFF59D), 10f, 0.5f, 0.15f, 3300, 250),
            Confetto(8, Color(0xFFFFF59D), 8f, 0.65f, 0.55f, 3700, 450)
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .zIndex(10f) // 🎯 CORRECTION : Z-index élevé pour être au-dessus
    ) {
        confetti.forEach { confetto ->
            AnimatedConfetto(confetto = confetto)
        }
    }
}

@Composable
private fun AnimatedConfetto(confetto: Confetto) {
    val infiniteTransition = rememberInfiniteTransition(label = "confetto_${confetto.id}")

    // Animation d'opacité (pulsation)
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = confetto.duration,
                delayMillis = confetto.delay,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha_${confetto.id}"
    )

    // Animation de chute (mouvement vertical)
    val fallProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = confetto.duration + 2000,
                delayMillis = confetto.delay,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "fall_${confetto.id}"
    )

    Box(
        modifier = Modifier
            .offset(
                x = (confetto.startX * 400).dp,
                y = ((confetto.startY * 800) + (fallProgress * 200)).dp // Chute progressive
            )
            .size(confetto.size.dp)
            .clip(CircleShape)
            .alpha(alpha * 0.85f)
            .background(confetto.color)
            .zIndex(10f) // 🎯 CORRECTION : Chaque confetti au-dessus aussi
    )
}

/**
 * Helper pour éviter l'erreur de remember
 */
@Composable
private fun remember(block: () -> List<Confetto>): List<Confetto> {
    return androidx.compose.runtime.remember { block() }
}
