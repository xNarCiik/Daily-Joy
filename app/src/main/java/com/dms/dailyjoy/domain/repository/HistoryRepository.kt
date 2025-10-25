package com.dms.dailyjoy.domain.repository

import com.dms.dailyjoy.data.database.entity.PleasureHistoryEntry
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getHistoryForDateRange(startDate: Long, endDate: Long): Flow<List<PleasureHistoryEntry>>
}