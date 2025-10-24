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
    val id: String,
    @StringRes val label: Int,
    val icon: ImageVector,
    val gradientColors: List<Color>
) {
    ALL(
        id = "all",
        label = R.string.category_all,
        icon = Icons.Default.AutoAwesome,
        gradientColors = listOf(Color(0xFFFFB4A2), Color(0xFFF8D7DA))
    ),

    FOOD(
        id = "food",
        label = R.string.category_food,
        icon = Icons.Default.Fastfood,
        gradientColors = listOf(Color(0xFFF48B77), Color(0xFFF9D48A))
    ),

    ENTERTAINMENT(
        id = "entertainment",
        label = R.string.category_entertainment,
        icon = Icons.Default.Theaters,
        gradientColors = listOf(Color(0xFFB980F0), Color(0xFFE580F0))
    ),

    SOCIAL(
        id = "social",
        label = R.string.category_social,
        icon = Icons.Default.People,
        gradientColors = listOf(Color(0xFF64B5F6), Color(0xFF42A5F5))
    ),

    WELLNESS(
        id = "wellness",
        label = R.string.category_wellness,
        icon = Icons.Default.Spa,
        gradientColors = listOf(Color(0xFFA8DADC), Color(0xFFC9ADA7))
    ),

    CREATIVE(
        id = "creative",
        label = R.string.category_creative,
        icon = Icons.Default.Palette,
        gradientColors = listOf(Color(0xFFFFE66D), Color(0xFFFFB4A2))
    ),

    OUTDOOR(
        id = "outdoor",
        label = R.string.category_outdoor,
        icon = Icons.Default.Landscape,
        gradientColors = listOf(Color(0xFF67D28E), Color(0xFFA4E1AE))
    ),

    SPORT(
        id = "sport",
        label = R.string.category_sport,
        icon = Icons.Default.FitnessCenter,
        gradientColors = listOf(Color(0xFF81C784), Color(0xFF66BB6A))
    ),

    SHOPPING(
        id = "shopping",
        label = R.string.category_shopping,
        icon = Icons.Default.ShoppingCart,
        gradientColors = listOf(Color(0xFF74A8E5), Color(0xFF9ECEF5))
    ),

    CULTURE(
        id = "culture",
        label = R.string.category_culture,
        icon = Icons.Default.Museum,
        gradientColors = listOf(Color(0xFFC9A87C), Color(0xFFE4C39B))
    ),

    LEARNING(
        id = "learning",
        label = R.string.category_learning,
        icon = Icons.Default.School,
        gradientColors = listOf(Color(0xFF6FCF97), Color(0xFF66D2B3))
    ),

    OTHER(
        id = "other",
        label = R.string.category_other,
        icon = Icons.Default.MoreHoriz,
        gradientColors = listOf(Color(0xFFBDBDBD), Color(0xFFE0E0E0))
    ),

    /*DEFAULT_CATEGORIES = listOf(
       ALL,
       FOOD,
       ENTERTAINMENT,
       SOCIAL,
       WELLNESS,
       CREATIVE,
       OUTDOOR,
       SPORT,
       SHOPPING,
       CULTURE,
       LEARNING,
       OTHER
   ) */
}
