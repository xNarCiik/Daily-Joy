package com.dms.flip.domain.repository

import com.dms.flip.domain.model.PleasureHistory
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getHistoryForDateRange(startDate: Long, endDate: Long): Flow<List<PleasureHistory>>
}