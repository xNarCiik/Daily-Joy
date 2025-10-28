package com.dms.dailyjoy.data.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Museum
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Theaters
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.dms.dailyjoy.R

enum class PleasureCategory(
    @StringRes val label: Int,
    val icon: ImageVector,
    val iconTint: Color
) {
    ALL(
        label = R.string.category_all,
        icon = Icons.Default.AutoAwesome,
        iconTint = Color(0xFFF59E0B) // Amber
    ),

    FOOD(
        label = R.string.category_food,
        icon = Icons.Default.Fastfood,
        iconTint = Color(0xFFEF4444) // Red
    ),

    ENTERTAINMENT(
        label = R.string.category_entertainment,
        icon = Icons.Default.Theaters,
        iconTint = Color(0xFF8B5CF6) // Purple
    ),

    SOCIAL(
        label = R.string.category_social,
        icon = Icons.Default.People,
        iconTint = Color(0xFF3B82F6) // Blue
    ),

    WELLNESS(
        label = R.string.category_wellness,
        icon = Icons.Default.Spa,
        iconTint = Color(0xFF06B6D4) // Cyan
    ),

    CREATIVE(
        label = R.string.category_creative,
        icon = Icons.Default.Palette,
        iconTint = Color(0xFFEC4899) // Pink
    ),

    OUTDOOR(
        label = R.string.category_outdoor,
        icon = Icons.Default.Landscape,
        iconTint = Color(0xFF10B981) // Green
    ),

    SPORT(
        label = R.string.category_sport,
        icon = Icons.Default.FitnessCenter,
        iconTint = Color(0xFF22C55E) // Lime
    ),

    SHOPPING(
        label = R.string.category_shopping,
        icon = Icons.Default.ShoppingCart,
        iconTint = Color(0xFF6366F1) // Indigo
    ),

    CULTURE(
        label = R.string.category_culture,
        icon = Icons.Default.Museum,
        iconTint = Color(0xFFA855F7) // Violet
    ),

    LEARNING(
        label = R.string.category_learning,
        icon = Icons.Default.School,
        iconTint = Color(0xFF14B8A6) // Teal
    ),

    OTHER(
        label = R.string.category_other,
        icon = Icons.Default.MoreHoriz,
        iconTint = Color(0xFF6B7280) // Gray
    )
}