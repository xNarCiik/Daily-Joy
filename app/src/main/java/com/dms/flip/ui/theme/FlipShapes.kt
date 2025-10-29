package com.dms.flip.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val AppShapes = Shapes(
    // (ex: Heure du rappel, Timer)
    small = RoundedCornerShape(12.dp),
    // (ex: Items de liste "Elodie Dubois")
    medium = RoundedCornerShape(16.dp),
    // (ex: Carte principale "Observer les nuages", carte "Aya Nakamura")
    large = RoundedCornerShape(28.dp),
    // (ex: Boutons "À demain", Chip "Pleine Conscience")
    extraLarge = RoundedCornerShape(50.dp)
)