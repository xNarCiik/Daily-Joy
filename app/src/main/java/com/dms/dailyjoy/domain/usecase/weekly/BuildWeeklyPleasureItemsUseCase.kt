package com.dms.dailyjoy.domain.usecase.weekly

import com.dms.dailyjoy.R
import com.dms.dailyjoy.domain.model.WeeklyPleasureDetails
import com.dms.dailyjoy.ui.weekly.PleasureStatus
import com.dms.dailyjoy.ui.weekly.WeeklyPleasureItem
import com.dms.dailyjoy.ui.util.getCurrentDayIndex
import javax.inject.Inject

class BuildWeeklyPleasureItemsUseCase @Inject constructor() {

    operator fun invoke(details: List<WeeklyPleasureDetails>): List<WeeklyPleasureItem> {
        val todayIndex = getCurrentDayIndex()

        val dayRes = listOf(
            R.string.full_day_monday,
            R.string.full_day_tuesday,
            R.string.full_day_wednesday,
            R.string.full_day_thursday,
            R.string.full_day_friday,
            R.string.full_day_saturday,
            R.string.full_day_sunday
        )

        return (0..6).map { dayOfWeek ->
            val detail = details.find { it.dayOfWeek == dayOfWeek }
            val currentDayIndex = dayOfWeek

            val status = when {
                currentDayIndex < todayIndex -> if (detail?.completed == true) PleasureStatus.PAST_COMPLETED else PleasureStatus.PAST_NOT_COMPLETED
                currentDayIndex == todayIndex -> if (detail?.completed == true) PleasureStatus.CURRENT_COMPLETED else if (detail?.pleasure?.isFlipped == true) PleasureStatus.CURRENT_REVEALED else PleasureStatus.LOCKED
                else -> PleasureStatus.LOCKED
            }

            WeeklyPleasureItem(
                dayNameRes = dayRes[currentDayIndex],
                pleasure = detail?.pleasure,
                status = status
            )
        }
    }
}
