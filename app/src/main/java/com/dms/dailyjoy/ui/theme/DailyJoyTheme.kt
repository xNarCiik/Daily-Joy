package com.dms.dailyjoy.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ========== LIGHT THEME COLORS ==========

private val LightPrimary = Color(0xFF6366F1)
private val LightOnPrimary = Color(0xFFFFFFFF)
private val LightPrimaryContainer = Color(0xFFE0E7FF)
private val LightOnPrimaryContainer = Color(0xFF1E1B4B)

private val LightSecondary = Color(0xFFEC4899)
private val LightOnSecondary = Color(0xFFFFFFFF)
private val LightSecondaryContainer = Color(0xFFFCE7F3)
private val LightOnSecondaryContainer = Color(0xFF831843)

private val LightTertiary = Color(0xFFF59E0B)
private val LightOnTertiary = Color(0xFFFFFFFF)
private val LightTertiaryContainer = Color(0xFFFEF3C7)
private val LightOnTertiaryContainer = Color(0xFF78350F)

private val LightBackground = Color(0xFFFFFBFE)
private val LightOnBackground = Color(0xFF1C1B1F)
private val LightSurface = Color(0xFFFFFFFF)
private val LightOnSurface = Color(0xFF1C1B1F)
private val LightSurfaceVariant = Color(0xFFF5F5F7)
private val LightOnSurfaceVariant = Color(0xFF49454F)

private val LightError = Color(0xFFDC2626)
private val LightOnError = Color(0xFFFFFFFF)
private val LightErrorContainer = Color(0xFFFEE2E2)
private val LightOnErrorContainer = Color(0xFF7F1D1D)

private val LightOutline = Color(0xFFE5E7EB)
private val LightOutlineVariant = Color(0xFFF3F4F6)

// ========== DARK THEME COLORS ==========

private val DarkPrimary = Color(0xFF818CF8)
private val DarkOnPrimary = Color(0xFF1E1B4B)
private val DarkPrimaryContainer = Color(0xFF312E81)
private val DarkOnPrimaryContainer = Color(0xFFE0E7FF)

private val DarkSecondary = Color(0xFFF472B6)
private val DarkOnSecondary = Color(0xFF831843)
private val DarkSecondaryContainer = Color(0xFF9F1239)
private val DarkOnSecondaryContainer = Color(0xFFFCE7F3)

private val DarkTertiary = Color(0xFFFBBF24)
private val DarkOnTertiary = Color(0xFF78350F)
private val DarkTertiaryContainer = Color(0xFF92400E)
private val DarkOnTertiaryContainer = Color(0xFFFEF3C7)

private val DarkBackground = Color(0xFF0F172A)
private val DarkOnBackground = Color(0xFFE2E8F0)
private val DarkSurface = Color(0xFF1E293B)
private val DarkOnSurface = Color(0xFFE2E8F0)
private val DarkSurfaceVariant = Color(0xFF334155)
private val DarkOnSurfaceVariant = Color(0xFFCBD5E1)

private val DarkError = Color(0xFFEF4444)
private val DarkOnError = Color(0xFF7F1D1D)
private val DarkErrorContainer = Color(0xFF7F1D1D)
private val DarkOnErrorContainer = Color(0xFFFEE2E2)

private val DarkOutline = Color(0xFF475569)
private val DarkOutlineVariant = Color(0xFF334155)

// ========== COLOR SCHEMES ==========

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,
    secondary = LightSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,
    tertiary = LightTertiary,
    onTertiary = LightOnTertiary,
    tertiaryContainer = LightTertiaryContainer,
    onTertiaryContainer = LightOnTertiaryContainer,
    error = LightError,
    onError = LightOnError,
    errorContainer = LightErrorContainer,
    onErrorContainer = LightOnErrorContainer,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightOutline,
    outlineVariant = LightOutlineVariant
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary = DarkTertiary,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    onTertiaryContainer = DarkOnTertiaryContainer,
    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant
)

@Composable
fun DailyJoyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
       /* dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        } */

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}

data class DailyJoyGradients(
    val background: Brush,
    val primary: Brush,
    val secondary: Brush,
    val accent: Brush,
    val header: Brush,
    val card: Brush,
    val success: Brush
)

@Composable
fun dailyJoyGradients(useDarkTheme: Boolean = isSystemInDarkTheme()): DailyJoyGradients {
    return DailyJoyGradients(
        background = DailyJoyGradient.background(useDarkTheme),
        primary = DailyJoyGradient.primary(useDarkTheme),
        secondary = DailyJoyGradient.secondary(useDarkTheme),
        accent = DailyJoyGradient.accent(useDarkTheme),
        header = DailyJoyGradient.header(useDarkTheme),
        card = DailyJoyGradient.card(useDarkTheme),
        success = DailyJoyGradient.success(useDarkTheme)
    )
}