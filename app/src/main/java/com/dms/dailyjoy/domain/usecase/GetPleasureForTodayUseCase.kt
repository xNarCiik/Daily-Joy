package com.dms.dailyjoy.domain.usecase

import com.dms.dailyjoy.data.model.Pleasure
import kotlinx.coroutines.flow.first
import java.util.Calendar
import javax.inject.Inject

class GetPleasureForTodayUseCase @Inject constructor(
    private val getWeeklyPleasuresUseCase: GetWeeklyPleasuresUseCase
) {
    suspend operator fun invoke(): Pleasure? =
        getWeeklyPleasuresUseCase().first().getOrNull(getCurrentDayId())

    private fun getCurrentDayId(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_WEEK)
    }
}
