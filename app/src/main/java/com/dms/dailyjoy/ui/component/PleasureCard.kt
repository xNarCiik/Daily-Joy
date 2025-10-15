package com.dms.dailyjoy.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.data.model.PleasureCategory
import com.dms.dailyjoy.data.model.PleasureType
import com.dms.dailyjoy.ui.theme.DailyJoyTheme

@Composable
fun PleasureCard(modifier: Modifier = Modifier, pleasure: Pleasure) {
    Card(
        modifier = modifier
            .width(width = 80.dp)
            .height(height = 120.dp)
            .padding(all = 16.dp),
        shape = RoundedCornerShape(size = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(all = 16.dp)) {
            Text(text = pleasure.title, style = MaterialTheme.typography.titleLarge)

            Spacer(Modifier.height(height = 8.dp))

            Text(
                text = "#${pleasure.category.name.lowercase().replaceFirstChar { it.uppercase() }}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PleasureCardPreview() {
    DailyJoyTheme {
        PleasureCard(
            pleasure = Pleasure(
                0, "Pleasure Title",
                description = "Pleasure Description",
                type = PleasureType.BIG,
                category = PleasureCategory.CREATIVE
            )
        )
    }
}