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
    // Pour les grands titres (ex: pas utilisé dans ton screenshot)
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = poppinsFamily),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = poppinsFamily),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = poppinsFamily),

    // "Sème une petite graine..." pourrait être un headlineSmall
    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = poppinsFamily),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = poppinsFamily),
    headlineSmall = defaultTypography.headlineSmall.copy(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.SemiBold // Un peu plus de poids pour l'accroche
    ),

    // "Faire un cheat meal XXL" pourrait être un titleLarge
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

    // "Ton plat préféré..." pourrait être un bodyLarge
    bodyLarge = defaultTypography.bodyLarge.copy(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Normal // Poids normal pour la lisibilité
    ),
    bodyMedium = defaultTypography.bodyMedium.copy(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Normal
    ),
    bodySmall = defaultTypography.bodySmall.copy(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Normal
    ),

    // "# Food" et le texte du bouton
    labelLarge = defaultTypography.labelLarge.copy(
        fontFamily = poppinsFamily,
        fontWeight = FontWeight.Medium // Medium pour les boutons/labels
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