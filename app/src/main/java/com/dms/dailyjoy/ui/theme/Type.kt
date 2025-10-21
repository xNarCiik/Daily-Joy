package com.dms.dailyjoy.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.dms.dailyjoy.R

private val poppinsFamily = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold)
)

val defaultTypography = Typography()

val AppTypography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = poppinsFamily),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = poppinsFamily),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = poppinsFamily),

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = poppinsFamily),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = poppinsFamily),
    headlineSmall = defaultTypography.headlineSmall.copy(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.SemiBold
    ),

    titleLarge = defaultTypography.titleLarge.copy(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.SemiBold
    ),
    titleMedium = defaultTypography.titleMedium.copy(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Medium
    ),
    titleSmall = defaultTypography.titleSmall.copy(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Medium
    ),

    bodyLarge = defaultTypography.bodyLarge.copy(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Normal
    ),
    bodyMedium = defaultTypography.bodyMedium.copy(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Normal
    ),
    bodySmall = defaultTypography.bodySmall.copy(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Normal
    ),

    labelLarge = defaultTypography.labelLarge.copy(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Medium
    ),
    labelMedium = defaultTypography.labelMedium.copy(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Medium
    ),
    labelSmall = defaultTypography.labelSmall.copy(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Medium
    )
)