package com.dms.flip.ui.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dms.flip.R
import com.dms.flip.ui.component.LoadingState
import com.dms.flip.ui.history.component.WeekNavigationHeader
import com.dms.flip.ui.history.component.WeeklyPleasuresList
import com.dms.flip.ui.history.component.WeeklyStatsGrid
import com.dms.flip.ui.theme.FlipTheme
import com.dms.flip.ui.util.LightDarkPreview
import com.dms.flip.ui.util.previewWeeklyDays

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    uiState: HistoryUiState,
    onEvent: (HistoryEvent) -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> {
                LoadingState(modifier = modifier.fillMaxSize())
            }

            uiState.error != null -> {
                Text(
                    text = stringResource(R.string.generic_error_message, uiState.error),
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                HistoryContent(
                    weeklyDays = uiState.weeklyDays,
                    weekTitle = uiState.weekTitle,
                    weekDates = uiState.weekDates,
                    streakDays = uiState.streakDays,
                    onEvent = onEvent
                )
            }
        }
    }
}

@Composable
private fun HistoryContent(
    modifier: Modifier = Modifier,
    weeklyDays: List<WeeklyDay>,
    weekTitle: String,
    weekDates: String,
    streakDays: Int,
    onEvent: (HistoryEvent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Header
        WeekNavigationHeader(
            weekTitle = weekTitle,
            weekDates = weekDates,
            onPreviousWeekClick = { onEvent(HistoryEvent.OnPreviousWeekClicked) },
            onNextWeekClick = { onEvent(HistoryEvent.OnNextWeekClicked) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Cartes de statistiques
        val history = weeklyDays.mapNotNull { it.historyEntry }
        val completedCount = history.count { it.completed }

        WeeklyStatsGrid(
            pleasuresCount = completedCount,
            streakDays = streakDays
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Liste des plaisirs hebdomadaires
        WeeklyPleasuresList(
            items = weeklyDays,
            onCardClicked = { item -> onEvent(HistoryEvent.OnCardClicked(item)) },
            onDiscoverTodayClicked = {
                onEvent(HistoryEvent.OnDiscoverTodayClicked)
            }
        )
    }
}

@LightDarkPreview
@Composable
private fun HistoryPreview() {
    FlipTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HistoryScreen(
                uiState = HistoryUiState(weeklyDays = previewWeeklyDays),
                onEvent = {}
            )
        }
    }
}
