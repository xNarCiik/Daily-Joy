package com.dms.flip.domain.usecase.weekly

import com.dms.flip.domain.model.PleasureHistory
import com.dms.flip.domain.model.WeeklyPleasuresStats
import javax.inject.Inject

class GetWeeklyPleasuresStatsUseCase @Inject constructor() {
    operator fun invoke(weeklyItems: List<PleasureHistory>): WeeklyPleasuresStats {
        return WeeklyPleasuresStats(completed = 2, remaining = 7) // TODO
    }
}
