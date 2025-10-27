package com.dms.dailyjoy.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = JoyPrimaryLight,
    onPrimary = JoyOnPrimaryLight,
    primaryContainer = JoyPrimaryContainerLight,
    onPrimaryContainer = JoyOnPrimaryContainerLight,
    secondary = JoySecondaryLight,
    onSecondary = JoyOnSecondaryLight,
    secondaryContainer = JoySecondaryContainerLight,
    onSecondaryContainer = JoyOnSecondaryContainerLight,
    tertiary = JoyTertiaryLight,
    onTertiary = JoyOnTertiaryLight,
    surface = JoySurfaceLight,
    onSurface = JoyOnSurfaceLight,
)

private val DarkColors = darkColorScheme(
    primary = JoyPrimaryDark,
    onPrimary = JoyOnPrimaryDark,
    primaryContainer = JoyPrimaryContainerDark,
    onPrimaryContainer = JoyOnPrimaryContainerDark,
    secondary = JoySecondaryDark,
    onSecondary = JoyOnSecondaryDark,
    secondaryContainer = JoySecondaryContainerDark,
    onSecondaryContainer = JoyOnSecondaryContainerDark,
    tertiary = JoyTertiaryDark,
    onTertiary = JoyOnTertiaryDark,
    surface = JoySurfaceDark,
    onSurface = JoyOnSurfaceDark,
)

@Composable
fun DailyJoyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}

data class DailyJoyGradients(
    val primary: Brush,
    val secondary: Brush,
    val accent: Brush
)

@Composable
fun dailyJoyGradients(useDarkTheme: Boolean = isSystemInDarkTheme()): DailyJoyGradients {
    return DailyJoyGradients(
        primary = DailyJoyGradient.primary(useDarkTheme),
        secondary = DailyJoyGradient.secondary(useDarkTheme),
        accent = DailyJoyGradient.accent(useDarkTheme)
    )
}
