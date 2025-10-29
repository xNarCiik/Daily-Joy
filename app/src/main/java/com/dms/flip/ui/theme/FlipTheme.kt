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






// TODO REMOVES ALL GRADIENTS AFTER TRANSITION OK
data class FlipGradients(
    val background: Brush,
    val primary: Brush,
    val secondary: Brush,
    val accent: Brush,
    val header: Brush,
    val card: Brush,
    val success: Brush
)

@Composable
fun flipGradients(useDarkTheme: Boolean = isSystemInDarkTheme()): FlipGradients {
    return FlipGradients(
        background = FlipGradient.background(useDarkTheme),
        primary = FlipGradient.primary(useDarkTheme),
        secondary = FlipGradient.secondary(useDarkTheme),
        accent = FlipGradient.accent(useDarkTheme),
        header = FlipGradient.header(useDarkTheme),
        card = FlipGradient.card(useDarkTheme),
        success = FlipGradient.success(useDarkTheme)
    )
}

object FlipGradient {
    fun background(isDark: Boolean): Brush {
        val colors = if(isDark) {
            listOf(
                Color(0xFF1A1A2E),
                Color(0xFF16213E),
                Color(0xFF0F3460)
            )
        } else {
            listOf(
                Color(0xFFFFF9E6),
                Color(0xFFFFF0F5),
                Color(0xFFE8F4F8)
            )
        }

        return Brush.verticalGradient(colors)
    }

    // Primary Gradient (Indigo -> Purple)
    fun primary(isDark: Boolean): Brush {
        val colors = if (isDark) {
            listOf(
                Color(0xFF6366F1), // Indigo
                Color(0xFF8B5CF6), // Purple
                Color(0xFFA78BFA)  // Light Purple
            )
        } else {
            listOf(
                Color(0xFF6366F1), // Indigo
                Color(0xFF8B5CF6), // Purple
                Color(0xFF7C3AED)  // Violet
            )
        }
        return Brush.horizontalGradient(colors)
    }

    // Secondary Gradient (Pink -> Orange)
    fun secondary(isDark: Boolean): Brush {
        val colors = if (isDark) {
            listOf(
                Color(0xFFF472B6), // Pink
                Color(0xFFFBBF24), // Amber
                Color(0xFFFCD34D)  // Light Amber
            )
        } else {
            listOf(
                Color(0xFFEC4899), // Pink
                Color(0xFFF59E0B), // Amber
                Color(0xFFF97316)  // Orange
            )
        }
        return Brush.horizontalGradient(colors)
    }

    // Accent Gradient (Purple -> Pink)
    fun accent(isDark: Boolean): Brush {
        val colors = if (isDark) {
            listOf(
                Color(0xFF8B5CF6), // Purple
                Color(0xFFF472B6)  // Pink
            )
        } else {
            listOf(
                Color(0xFF7C3AED), // Violet
                Color(0xFFEC4899)  // Pink
            )
        }
        return Brush.horizontalGradient(colors)
    }

    // Card/Container Gradient
    fun card(isDark: Boolean): Brush {
        val colors = if (isDark) {
            listOf(
                Color(0xFF312E81).copy(alpha = 0.7f),
                Color(0xFF9F1239).copy(alpha = 0.6f),
                Color(0xFF92400E).copy(alpha = 0.5f)
            )
        } else {
            listOf(
                Color(0xFFE0E7FF).copy(alpha = 0.8f),
                Color(0xFFFCE7F3).copy(alpha = 0.7f),
                Color(0xFFFEF3C7).copy(alpha = 0.6f)
            )
        }
        return Brush.verticalGradient(colors)
    }

    // Header Gradient (plus vibrant)
    fun header(isDark: Boolean): Brush {
        val colors = if (isDark) {
            listOf(
                Color(0xFF4338CA), // Indigo profond
                Color(0xFF7C3AED), // Purple
                Color(0xFF9333EA)  // Fuchsia
            )
        } else {
            listOf(
                Color(0xFF6366F1), // Indigo
                Color(0xFF8B5CF6), // Purple
                Color(0xFFEC4899)  // Pink
            )
        }
        return Brush.horizontalGradient(colors)
    }

    // Success/Completed Gradient
    fun success(isDark: Boolean): Brush {
        val colors = if (isDark) {
            listOf(
                Color(0xFF4338CA).copy(alpha = 0.8f),
                Color(0xFF7C3AED).copy(alpha = 0.7f),
                Color(0xFF9333EA).copy(alpha = 0.6f)
            )
        } else {
            listOf(
                Color(0xFF6366F1).copy(alpha = 0.7f),
                Color(0xFF8B5CF6).copy(alpha = 0.6f),
                Color(0xFFEC4899).copy(alpha = 0.5f)
            )
        }
        return Brush.verticalGradient(colors)
    }
}
