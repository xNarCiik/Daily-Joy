package com.dms.dailyjoy.ui.weekly

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dms.dailyjoy.R
import com.dms.dailyjoy.data.database.entity.PleasureHistoryEntry
import com.dms.dailyjoy.data.model.PleasureCategory
import com.dms.dailyjoy.ui.component.LoadingState
import com.dms.dailyjoy.ui.theme.DailyJoyTheme
import com.dms.dailyjoy.ui.util.LightDarkPreview
import com.dms.dailyjoy.ui.weekly.component.WeeklyPleasuresList
import com.dms.dailyjoy.ui.weekly.component.WeeklyStatsCard

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
                    history = uiState.history,
                    onEvent = onEvent
                )
            }
        }
    }
}

@Composable
private fun EmptyWeeklyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.weekly_empty_pleasures),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.weekly_start_today),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun WeeklyContent(
    modifier: Modifier = Modifier,
    history: List<PleasureHistoryEntry>,
    onEvent: (WeeklyEvent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val completedCount = history.count { it.isCompleted }
        WeeklyStatsCard(
            completedCount = completedCount,
            totalCount = history.size
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .height(2.dp)
                .clip(RoundedCornerShape(1.dp))
                .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        )

        Spacer(modifier = Modifier.height(24.dp))

        WeeklyPleasuresList(
            items = history,
            onCardClicked = { item -> onEvent(WeeklyEvent.OnCardClicked(item)) }
        )
    }
}

@LightDarkPreview
@Composable
private fun WeeklyEmptyPreview() {
    DailyJoyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
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
    DailyJoyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            WeeklyScreen(
                uiState = WeeklyUiState(
                    history = listOf(
                        PleasureHistoryEntry(
                            id = 1,
                            dayIdentifier = "",
                            dateDrawn = System.currentTimeMillis(),
                            isCompleted = true,
                            pleasureTitle = "Boire un café chaud",
                            pleasureDescription = "Savourer un bon café le matin.",
                            category = PleasureCategory.ALL
                        )
                    )
                ),
                onEvent = {}
            )
        }
    }
}
