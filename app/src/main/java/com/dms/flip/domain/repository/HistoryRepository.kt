package com.dms.flip.domain.repository

import com.dms.flip.data.model.PleasureHistoryEntry
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getHistoryForDateRange(startDate: Long, endDate: Long): Flow<List<PleasureHistoryEntry>>
}