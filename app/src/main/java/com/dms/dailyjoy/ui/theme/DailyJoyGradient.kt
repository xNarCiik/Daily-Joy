package com.dms.dailyjoy.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object DailyJoyGradient {

    @Composable
    fun primary(useDarkTheme: Boolean = isSystemInDarkTheme()): Brush {
        return if (useDarkTheme) {
            Brush.verticalGradient(
                listOf(
                    Color(0xFF1A1A2E),
                    Color(0xFF16213E),
                    Color(0xFF0F3460)
                )
            )
        } else {
            Brush.verticalGradient(
                listOf(
                    Color(0xFFFFF9E6),
                    Color(0xFFFFF0F5),
                    Color(0xFFE8F4F8)
                )
            )
        }
    }

    @Composable
    fun secondary(useDarkTheme: Boolean = isSystemInDarkTheme()): Brush {
        return if (useDarkTheme) {
            Brush.horizontalGradient(
                listOf(
                    Color(0xFF0F3460),
                    Color(0xFF53354A)
                )
            )
        } else {
            Brush.horizontalGradient(
                listOf(
                    Color(0xFFFFD8E4),
                    Color(0xFFFFF5B1)
                )
            )
        }
    }

    @Composable
    fun accent(useDarkTheme: Boolean = isSystemInDarkTheme()): Brush {
        return if (useDarkTheme) {
            Brush.radialGradient(
                listOf(
                    Color(0xFFFF6B9D).copy(alpha = 0.3f),
                    Color(0xFF1A1A2E)
                )
            )
        } else {
            Brush.radialGradient(
                listOf(
                    Color(0xFFFFB6C1).copy(alpha = 0.25f),
                    Color(0xFFFFF9E6)
                )
            )
        }
    }
}
