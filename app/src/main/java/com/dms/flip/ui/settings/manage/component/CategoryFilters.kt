package com.dms.flip.ui.settings.manage.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dms.flip.data.model.PleasureCategory
import com.dms.flip.ui.theme.FlipTheme
import com.dms.flip.ui.util.LightDarkPreview

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
                        text = stringResource(category.label),
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
    FlipTheme {
        CategoryFilters(
            selectedCategories = setOf(PleasureCategory.FOOD, PleasureCategory.CREATIVE),
            onCategorySelected = {}
        )
    }
}
