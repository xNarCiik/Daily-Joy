package com.dms.flip.domain.usecase.weekly

import com.dms.flip.data.model.PleasureHistoryEntry
import com.dms.flip.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import javax.inject.Inject

class GetWeeklyHistoryUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) {
    operator fun invoke(): Flow<List<PleasureHistoryEntry>> {
        val calendar = Calendar.getInstance()

        // End date
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val endDate = calendar.timeInMillis

        // Start Date
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val startDate = calendar.timeInMillis

        return historyRepository.getHistoryForDateRange(startDate, endDate)
    }
}