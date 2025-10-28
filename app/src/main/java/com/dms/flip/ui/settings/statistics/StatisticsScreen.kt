package com.dms.flip.ui.settings.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dms.flip.R
import com.dms.flip.ui.settings.statistics.component.CategoryStatsCard
import com.dms.flip.ui.settings.statistics.component.DetailedStatsCard
import com.dms.flip.ui.settings.statistics.component.MonthlyProgressCard
import com.dms.flip.ui.settings.statistics.component.StatCard
import com.dms.flip.ui.theme.FlipTheme
import com.dms.flip.ui.util.LightDarkPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    uiState: StatisticsUiState = StatisticsUiState(),
    onEvent: (StatisticsEvent) -> Unit = {},
    onNavigateBack: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.statistics_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.EmojiEvents,
                    title = stringResource(R.string.statistics_total_pleasures_title),
                    value = uiState.totalPleasures.toString(),
                    subtitle = stringResource(R.string.statistics_pleasures_subtitle),
                    gradient = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                    ),
                    iconTint = MaterialTheme.colorScheme.primary
                )

                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.LocalFireDepartment,
                    title = stringResource(R.string.statistics_streak_title),
                    value = uiState.currentStreak.toString(),
                    subtitle = stringResource(R.string.statistics_days_subtitle),
                    gradient = listOf(
                        Color(0xFFFF6B6B).copy(alpha = 0.15f),
                        Color(0xFFFF6B6B).copy(alpha = 0.05f)
                    ),
                    iconTint = Color(0xFFFF6B6B)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.TrendingUp,
                    title = stringResource(id = R.string.statistics_average_per_day),
                    value = uiState.averagePerDay.toString(),
                    subtitle = stringResource(id = R.string.statistics_this_month),
                    gradient = listOf(
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f),
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f)
                    ),
                    iconTint = MaterialTheme.colorScheme.secondary
                )

                StatCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.CalendarMonth,
                    title = stringResource(id = R.string.statistics_active_days),
                    value = uiState.activeDays.toString(),
                    subtitle = stringResource(id = R.string.statistics_in_total),
                    gradient = listOf(
                        Color(0xFF4CAF50).copy(alpha = 0.15f),
                        Color(0xFF4CAF50).copy(alpha = 0.05f)
                    ),
                    iconTint = Color(0xFF4CAF50)
                )
            }

            MonthlyProgressCard(uiState.monthlyProgress)

            CategoryStatsCard(uiState.favoriteCategories)

            DetailedStatsCard(uiState.detailedStats)
        }
    }
}

@LightDarkPreview
@Composable
private fun StatisticsScreenPreview() {
    FlipTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            StatisticsScreen()
        }
    }
}
