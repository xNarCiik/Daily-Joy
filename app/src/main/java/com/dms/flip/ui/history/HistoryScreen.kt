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
import com.dms.flip.data.database.entity.PleasureHistoryEntry
import com.dms.flip.data.model.PleasureCategory
import com.dms.flip.ui.component.LoadingState
import com.dms.flip.ui.history.component.WeekNavigationHeader
import com.dms.flip.ui.history.component.WeeklyPleasuresList
import com.dms.flip.ui.history.component.WeeklyStatsGrid
import com.dms.flip.ui.theme.FlipTheme
import com.dms.flip.ui.util.LightDarkPreview

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
                    // TODO: Récupérer le titre de la semaine depuis le ViewModel (ex: "Cette Semaine")
                    weekTitle = "Cette Semaine",
                    // TODO: Calculer les dates de la semaine dans le ViewModel (ex: "17 - 23 Juin")
                    weekDates = "17 - 23 Juin",
                    // TODO: Récupérer le streak depuis le ViewModel (nombre de jours consécutifs)
                    streakDays = 5,
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
        // Header avec navigation hebdomadaire
        WeekNavigationHeader(
            weekTitle = weekTitle,
            weekDates = weekDates,
            onPreviousWeekClick = {
                // TODO: Lier au ViewModel (ex: viewModel.onPreviousWeekClicked())
            },
            onNextWeekClick = {
                // TODO: Lier au ViewModel (ex: viewModel.onNextWeekClicked())
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Cartes de statistiques (2 colonnes)
        val history = weeklyDays.mapNotNull { it.historyEntry }
        val completedCount = history.count { it.isCompleted }

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
                // TODO: Lier au ViewModel pour naviguer vers l'écran du plaisir du jour
                // (ex: viewModel.onDiscoverTodayClicked())
            }
        )
    }
}

// ========== PREVIEWS ==========
@LightDarkPreview
@Composable
private fun HistoryEmptyPreview() {
    FlipTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HistoryScreen(
                uiState = HistoryUiState(),
                onEvent = {}
            )
        }
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
                uiState = HistoryUiState(
                    weeklyDays = listOf(
                        WeeklyDay(
                            dayName = "Lundi",
                            historyEntry = PleasureHistoryEntry(
                                id = 1,
                                dayIdentifier = "",
                                dateDrawn = System.currentTimeMillis() - 86400000 * 2,
                                isCompleted = true,
                                pleasureTitle = "Savourer un café chaud",
                                pleasureDescription = "Prendre le temps de déguster",
                                category = PleasureCategory.FOOD
                            )
                        ),
                        WeeklyDay(
                            dayName = "Mardi",
                            historyEntry = PleasureHistoryEntry(
                                id = 2,
                                dayIdentifier = "",
                                dateDrawn = System.currentTimeMillis() - 86400000,
                                isCompleted = true,
                                pleasureTitle = "Lire quelques pages d'un livre",
                                pleasureDescription = "Se plonger dans une histoire",
                                category = PleasureCategory.LEARNING
                            )
                        ),
                        WeeklyDay(
                            dayName = "Mercredi",
                            historyEntry = PleasureHistoryEntry(
                                id = 3,
                                dayIdentifier = "",
                                dateDrawn = System.currentTimeMillis(),
                                isCompleted = false,
                                pleasureTitle = "Plaisir du jour",
                                pleasureDescription = "",
                                category = PleasureCategory.ALL
                            )
                        ),
                        WeeklyDay(dayName = "Jeudi", historyEntry = null),
                        WeeklyDay(dayName = "Vendredi", historyEntry = null),
                        WeeklyDay(dayName = "Samedi", historyEntry = null),
                        WeeklyDay(dayName = "Dimanche", historyEntry = null)
                    )
                ),
                onEvent = {}
            )
        }
    }
}
