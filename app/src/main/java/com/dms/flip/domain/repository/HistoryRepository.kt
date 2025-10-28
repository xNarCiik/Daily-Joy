package com.dms.flip.domain.repository

import com.dms.flip.data.database.entity.PleasureHistoryEntry
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getHistoryForDateRange(startDate: Long, endDate: Long): Flow<List<PleasureHistoryEntry>>
}