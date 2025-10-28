package com.dms.flip.ui.weekly

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dms.flip.R
import com.dms.flip.data.database.entity.PleasureHistoryEntry
import com.dms.flip.data.model.PleasureCategory
import com.dms.flip.ui.component.LoadingState
import com.dms.flip.ui.theme.FlipTheme
import com.dms.flip.ui.util.LightDarkPreview
import com.dms.flip.ui.weekly.component.WeeklyPleasuresList
import com.dms.flip.ui.weekly.component.WeeklyStatsCard

@Composable
fun WeeklyScreen(
    modifier: Modifier = Modifier,
    uiState: WeeklyUiState,
    onEvent: (WeeklyEvent) -> Unit
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

            uiState.isEmpty -> {
                EmptyWeeklyState(modifier = Modifier.align(Alignment.Center))
            }

            else -> {
                WeeklyContent(
                    weeklyDays = uiState.weeklyDays,
                    onEvent = onEvent
                )
            }
        }
    }
}

@Composable
private fun EmptyWeeklyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(44.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.weekly_empty_pleasures),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.weekly_start_today),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun WeeklyContent(
    modifier: Modifier = Modifier,
    weeklyDays: List<WeeklyDay>,
    onEvent: (WeeklyEvent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val history = weeklyDays.mapNotNull { it.historyEntry }
        val completedCount = history.count { it.isCompleted }
        WeeklyStatsCard(
            completedCount = completedCount,
            totalCount = history.size
        )

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(3.dp)
                .clip(RoundedCornerShape(1.5.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                        )
                    )
                )
        )

        Spacer(modifier = Modifier.height(28.dp))

        WeeklyPleasuresList(
            items = weeklyDays,
            onCardClicked = { item -> onEvent(WeeklyEvent.OnCardClicked(item)) }
        )
    }
}

@LightDarkPreview
@Composable
private fun WeeklyEmptyPreview() {
    FlipTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            WeeklyScreen(
                uiState = WeeklyUiState(),
                onEvent = {}
            )
        }
    }
}

@LightDarkPreview
@Composable
private fun WeeklyPreview() {
    FlipTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            WeeklyScreen(
                uiState = WeeklyUiState(
                    weeklyDays = listOf(
                        WeeklyDay(
                            dayName = "Lundi",
                            historyEntry = PleasureHistoryEntry(
                                id = 1,
                                dayIdentifier = "",
                                dateDrawn = System.currentTimeMillis(),
                                isCompleted = true,
                                pleasureTitle = "Boire un café chaud",
                                pleasureDescription = "Savourer un bon café le matin.",
                                category = PleasureCategory.ALL
                            )
                        ),
                        WeeklyDay(dayName = "Mardi", historyEntry = null),
                        WeeklyDay(
                            dayName = "Mercredi",
                            historyEntry = PleasureHistoryEntry(
                                id = 3,
                                dayIdentifier = "",
                                dateDrawn = System.currentTimeMillis(),
                                isCompleted = true,
                                pleasureTitle = "Lire un livre",
                                pleasureDescription = "Lire un chapitre de mon livre préféré.",
                                category = PleasureCategory.LEARNING
                            )
                        )
                    )
                ),
                onEvent = {}
            )
        }
    }
}
