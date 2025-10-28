package com.dms.flip.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush

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
        content = content
    )
}

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