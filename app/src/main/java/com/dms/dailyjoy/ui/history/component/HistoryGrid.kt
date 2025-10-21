package com.dms.dailyjoy.ui.history.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.ui.history.WeeklyPleasureItem
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview
import com.dms.dailyjoy.ui.util.previewHistoryUiState

@Composable
fun HistoryGrid(
    modifier: Modifier = Modifier,
    items: List<WeeklyPleasureItem>,
    onCardClicked: (WeeklyPleasureItem) -> Unit
) {
    if (items.size != 7) return

    val (row1, row2) = items.chunked(4)

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            row1.forEach { item ->
                HistoryCard(
                    modifier = Modifier.weight(1f),
                    item = item,
                    onClick = { onCardClicked(item) }
                )
            }
        }

        Spacer(Modifier.height(22.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.weight(0.5f))

            row2.forEach { item ->
                HistoryCard(
                    modifier = Modifier.weight(1f),
                    item = item,
                    onClick = { onCardClicked(item) }
                )
            }

            Spacer(modifier = Modifier.weight(0.5f))
        }
    }
}

@LightDarkPreview
@Composable
private fun HistoryGridPreview() {
    DailyJoyTheme {
        HistoryGrid(
            items = previewHistoryUiState.weeklyPleasures,
            onCardClicked = {}
        )
    }
}
