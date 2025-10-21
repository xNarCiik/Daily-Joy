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
        else -> CategoryInfo( // TODO FILL OTHER PLEASURE
            label = stringResource(R.string.category_other),
            color = Color(0xFF607D8B)
        )
    }
}

@Composable
fun PleasureCategory.getLabel(): String = this.toCategoryInfo().label
