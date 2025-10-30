package com.dms.flip.domain.usecase.weekly

import com.dms.flip.data.model.PleasureHistoryEntry
import com.dms.flip.domain.model.WeeklyPleasuresStats
import javax.inject.Inject

class GetWeeklyPleasuresStatsUseCase @Inject constructor() {
    operator fun invoke(weeklyItems: List<PleasureHistoryEntry>): WeeklyPleasuresStats {
        return WeeklyPleasuresStats(completed = 2, remaining = 7) // TODO
    }
}
