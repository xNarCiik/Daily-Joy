package com.dms.dailyjoy.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.dms.dailyjoy.R
import com.dms.dailyjoy.data.model.PleasureCategory

data class CategoryInfo(
    val label: String,
    val color: Color
)

@Composable
fun PleasureCategory.toCategoryInfo(): CategoryInfo {
    return when (this) {
        PleasureCategory.FOOD -> CategoryInfo(
            label = stringResource(R.string.category_food),
            color = Color(0xFFFF9800)
        )
        PleasureCategory.ENTERTAINMENT -> CategoryInfo(
            label = stringResource(R.string.category_entertainment),
            color = Color(0xFF9C27B0)
        )
        PleasureCategory.SOCIAL -> CategoryInfo(
            label = stringResource(R.string.category_social),
            color = Color(0xFF2196F3)
        )
        PleasureCategory.WELLNESS -> CategoryInfo(
            label = stringResource(R.string.category_wellness),
            color = Color(0xFF4CAF50)
        )
        PleasureCategory.CREATIVE -> CategoryInfo(
            label = stringResource(R.string.category_creative),
            color = Color(0xFFE91E63)
        )
        PleasureCategory.OUTDOOR -> CategoryInfo(
            label = stringResource(R.string.category_outdoor),
            color = Color(0xFF009688)
        )
        PleasureCategory.SPORT -> CategoryInfo(
            label = stringResource(R.string.category_sport),
            color = Color(0xFFF44336)
        )
        PleasureCategory.SHOPPING -> CategoryInfo(
            label = stringResource(R.string.category_shopping),
            color = Color(0xFF795548)
        )
        PleasureCategory.CULTURE -> CategoryInfo(
            label = stringResource(R.string.category_culture),
            color = Color(0xFF673AB7)
        )
        PleasureCategory.LEARNING -> CategoryInfo(
            label = stringResource(R.string.category_learning),
            color = Color(0xFF3F51B5)
        )
        PleasureCategory.OTHER -> CategoryInfo(
            label = stringResource(R.string.category_other),
            color = Color(0xFF607D8B)
        )
    }
}

@Composable
fun PleasureCategory.getLabel(): String = this.toCategoryInfo().label
