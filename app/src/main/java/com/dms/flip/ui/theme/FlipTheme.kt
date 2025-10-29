package com.dms.flip.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun FlipTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
object FlipGradient {

    /**
     * Gradient principal pour la carte (lavande → bleu clair)
     */
    fun cardGradient(isDark: Boolean): Brush {
        return if (isDark) {
            Brush.linearGradient(
                colors = listOf(
                    Color(0xFFB39DDB), // Lavender
                    Color(0xFF90CAF9)  // Light blue
                )
            )
        } else {
            Brush.linearGradient(
                colors = listOf(
                    Color(0xFFCE93D8), // Light lavender
                    Color(0xFF81D4FA)  // Lighter blue
                )
            )
        }
    }

    /**
     * Gradient secondaire pour les boutons d'action (pêche/coral)
     * Conforme à l'image 2 (bouton "Partager")
     */
    fun actionButtonGradient(isDark: Boolean): Brush {
        return if (isDark) {
            Brush.linearGradient(
                colors = listOf(
                    Color(0xFFFFAB91), // Coral
                    Color(0xFFFF8A80)  // Light coral
                )
            )
        } else {
            Brush.linearGradient(
                colors = listOf(
                    Color(0xFFFF8A65), // Darker coral
                    Color(0xFFFF7043)  // Deeper coral
                )
            )
        }
    }

    /**
     * Gradient pour le fond de setup (conservé de l'ancien système si nécessaire)
     */
    fun setupBackground(isDark: Boolean): Brush {
        return if (isDark) {
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF1A1A2E),
                    Color(0xFF16213E),
                    Color(0xFF0F3460)
                )
            )
        } else {
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFFFF9E6),
                    Color(0xFFFFF0F5),
                    Color(0xFFE8F4F8)
                )
            )
        }
    }
}

/**
 * Helper pour accéder facilement aux gradients dans les Composables
 */
data class FlipGradients(
    val card: Brush,
    val actionButton: Brush,
    val setupBackground: Brush
)

@Composable
fun flipGradients(useDarkTheme: Boolean = isSystemInDarkTheme()): FlipGradients {
    return FlipGradients(
        card = FlipGradient.cardGradient(useDarkTheme),
        actionButton = FlipGradient.actionButtonGradient(useDarkTheme),
        setupBackground = FlipGradient.setupBackground(useDarkTheme)
    )
}