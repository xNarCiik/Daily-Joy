package com.dms.dailyjoy.ui.util

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview
import com.dms.dailyjoy.R
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.data.model.PleasureCategory
import com.dms.dailyjoy.data.model.PleasureType
import com.dms.dailyjoy.ui.dailypleasure.DailyPleasureUiState
import com.dms.dailyjoy.ui.history.HistoryUiState
import com.dms.dailyjoy.ui.history.PleasureStatus
import com.dms.dailyjoy.ui.history.WeeklyPleasureItem

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
annotation class LightDarkPreview

val previewDailyPleasure = Pleasure(
    id = 0,
    title = "Pleasure Title",
    description = "Pleasure Description",
    type = PleasureType.BIG,
    category = PleasureCategory.CREATIVE,
    isFlipped = true
)

val previewDailyPleasureUiState = DailyPleasureUiState(
    isLoading = false,
    dailyMessage = "Et si aujourd'hui, on prenait le temps de...",
    dailyPleasure = previewDailyPleasure
)

val previewHistoryUiState = HistoryUiState(
    isLoading = false,
    weeklyPleasures = listOf(
        WeeklyPleasureItem(
            dayNameRes = R.string.day_monday,
            status = PleasureStatus.PAST_NOT_COMPLETED,
            pleasure = previewDailyPleasure
        ),
        WeeklyPleasureItem(
            dayNameRes = R.string.day_tuesday,
            status = PleasureStatus.PAST_COMPLETED,
            pleasure = previewDailyPleasure
        ),
        WeeklyPleasureItem(
            dayNameRes = R.string.day_wednesday,
            status = PleasureStatus.CURRENT_COMPLETED,
            pleasure = previewDailyPleasure
        ),
        WeeklyPleasureItem(
            dayNameRes = R.string.day_thursday,
            status = PleasureStatus.CURRENT_REVEALED,
            pleasure = previewDailyPleasure
        ),
        WeeklyPleasureItem(
            dayNameRes = R.string.day_friday,
            status = PleasureStatus.LOCKED,
            pleasure = previewDailyPleasure
        ),
        WeeklyPleasureItem(
            dayNameRes = R.string.day_saturday,
            status = PleasureStatus.LOCKED,
            pleasure = previewDailyPleasure
        ),
        WeeklyPleasureItem(
            dayNameRes = R.string.day_sunday,
            status = PleasureStatus.LOCKED,
            pleasure = previewDailyPleasure
        )
    ),
    completedPleasuresCount = 2,
    remainingPleasuresCount = 5
)
