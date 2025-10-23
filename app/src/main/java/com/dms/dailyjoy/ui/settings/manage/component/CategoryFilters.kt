package com.dms.dailyjoy.ui.settings.manage.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.data.model.PleasureCategory
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview
import com.dms.dailyjoy.ui.util.getLabel

@Composable
fun CategoryFilters(
    modifier: Modifier = Modifier,
    selectedCategories: Set<PleasureCategory>,
    onCategorySelected: (PleasureCategory) -> Unit,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = PleasureCategory.entries,
            key = { it.name }
        ) { category ->
            FilterChip(
                selected = category in selectedCategories,
                onClick = { onCategorySelected(category) },
                label = {
                    Text(
                        text = category.getLabel(),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            )
        }
    }
}

@LightDarkPreview
@Composable
private fun CategoryFiltersPreview() {
    DailyJoyTheme {
        CategoryFilters(
            selectedCategories = setOf(PleasureCategory.FOOD, PleasureCategory.CREATIVE),
            onCategorySelected = {}
        )
    }
}
