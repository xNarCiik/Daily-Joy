package com.dms.dailyjoy.ui.util

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview
import com.dms.dailyjoy.R
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.data.model.PleasureCategory
import com.dms.dailyjoy.data.model.PleasureType
import com.dms.dailyjoy.ui.social.Friend
import com.dms.dailyjoy.ui.social.FriendPleasure
import com.dms.dailyjoy.ui.weekly.PleasureStatus
import com.dms.dailyjoy.ui.weekly.WeeklyPleasureItem
import com.dms.dailyjoy.ui.weekly.WeeklyUiState

@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
annotation class LightDarkPreview

val previewDailyPleasure = Pleasure(
    id = 0,
    title = "Pleasure Title",
    description = "Pleasure Description",
    type = PleasureType.BIG,
    category = PleasureCategory.CREATIVE
)

val previewWeeklyUiState = WeeklyUiState(
    isLoading = false,
    weeklyPleasures = listOf(
        WeeklyPleasureItem(
            dayNameRes = R.string.full_day_monday,
            status = PleasureStatus.PAST_NOT_COMPLETED,
            pleasure = previewDailyPleasure
        ),
        WeeklyPleasureItem(
            dayNameRes = R.string.full_day_tuesday,
            status = PleasureStatus.PAST_COMPLETED,
            pleasure = previewDailyPleasure
        ),
        WeeklyPleasureItem(
            dayNameRes = R.string.full_day_wednesday,
            status = PleasureStatus.CURRENT_COMPLETED,
            pleasure = previewDailyPleasure
        ),
        WeeklyPleasureItem(
            dayNameRes = R.string.full_day_thursday,
            status = PleasureStatus.CURRENT_REVEALED,
            pleasure = previewDailyPleasure
        ),
        WeeklyPleasureItem(
            dayNameRes = R.string.full_day_friday,
            status = PleasureStatus.LOCKED,
            pleasure = previewDailyPleasure
        ),
        WeeklyPleasureItem(
            dayNameRes = R.string.full_day_saturday,
            status = PleasureStatus.LOCKED,
            pleasure = previewDailyPleasure
        ),
        WeeklyPleasureItem(
            dayNameRes = R.string.full_day_sunday,
            status = PleasureStatus.LOCKED,
            pleasure = previewDailyPleasure
        )
    ),
    completedPleasuresCount = 2,
    remainingPleasuresCount = 5
)

val previewFriends = listOf(
    Friend(
        id = "1",
        username = "Dams",
        streak = 8,
        currentPleasure = FriendPleasure(
            title = "Roule un pet (ou 2...)",
            status = com.dms.dailyjoy.ui.social.PleasureStatus.COMPLETED
        )
    ),
    Friend(
        id = "2",
        username = "Emma",
        streak = 326,
        currentPleasure = FriendPleasure(
            title = "Baiser Hugo",
            status = com.dms.dailyjoy.ui.social.PleasureStatus.IN_PROGRESS
        )
    ),
    Friend(
        id = "3",
        username = "Alisson",
        streak = 2,
        currentPleasure = FriendPleasure(
            title = "Faire une bouffe XXL",
            status = com.dms.dailyjoy.ui.social.PleasureStatus.IN_PROGRESS
        )
    ),
    Friend(
        id = "4",
        username = "Lilou la fripouille",
        streak = 33,
        currentPleasure = FriendPleasure(
            title = "Appeler mon petit frère et lui tapper la causette h24",
            status = com.dms.dailyjoy.ui.social.PleasureStatus.COMPLETED
        )
    )
)