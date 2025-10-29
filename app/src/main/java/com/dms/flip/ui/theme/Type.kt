package com.dms.flip.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dms.flip.R

val plusJakartaSansFamily = FontFamily(
    Font(R.font.plus_jakarta_sans_regular, FontWeight.Normal),
    Font(R.font.plus_jakarta_sans_medium, FontWeight.Medium),
    Font(R.font.plus_jakarta_sans_semibold, FontWeight.SemiBold),
    Font(R.font.plus_jakarta_sans_bold, FontWeight.Bold)
)

val AppTypography = Typography(
    // ex: Lettre initiale dans grand avatar
    headlineLarge = TextStyle(
        fontFamily = plusJakartaSansFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        lineHeight = 56.sp
    ),
    // ex: "Bravo !", "Amis", "Réglages"
    headlineMedium = TextStyle(
        fontFamily = plusJakartaSansFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp
    ),
    // ex: "Votre plaisir du jour"
    headlineSmall = TextStyle(
        fontFamily = plusJakartaSansFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp
    ),
    // ex: "Observer les nuages"
    titleLarge = TextStyle(
        fontFamily = plusJakartaSansFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    // ex: "Aya Nakamura", "Elodie Dubois"
    titleMedium = TextStyle(
        fontFamily = plusJakartaSansFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    // ex: "Prenez un moment pour vous..."
    bodyLarge = TextStyle(
        fontFamily = plusJakartaSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    // ex: "aya.nakamura@email.com", "Savourer un café chaud"
    bodyMedium = TextStyle(
        fontFamily = plusJakartaSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    // ex: Emails secondaires, sous-titres
    bodySmall = TextStyle(
        fontFamily = plusJakartaSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    // ex: "Pleine Conscience", "À demain !"
    labelLarge = TextStyle(
        fontFamily = plusJakartaSansFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    // ex: Labels de la Bottom Nav Bar
    labelMedium = TextStyle(
        fontFamily = plusJakartaSansFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
)
