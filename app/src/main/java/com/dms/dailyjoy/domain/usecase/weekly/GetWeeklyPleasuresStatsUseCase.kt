package com.dms.dailyjoy.domain.usecase.weekly

import com.dms.dailyjoy.domain.model.WeeklyPleasuresStats
import com.dms.dailyjoy.ui.weekly.PleasureStatus
import com.dms.dailyjoy.ui.weekly.WeeklyPleasureItem
import javax.inject.Inject

class GetWeeklyPleasuresStatsUseCase @Inject constructor() {
    operator fun invoke(weeklyItems: List<WeeklyPleasureItem>): WeeklyPleasuresStats {
        val completedCount = weeklyItems.count { it.status == PleasureStatus.PAST_COMPLETED || it.status == PleasureStatus.CURRENT_COMPLETED }
        val remainingCount = 7 - completedCount
        return WeeklyPleasuresStats(completed = completedCount, remaining = remainingCount)
    }
}
