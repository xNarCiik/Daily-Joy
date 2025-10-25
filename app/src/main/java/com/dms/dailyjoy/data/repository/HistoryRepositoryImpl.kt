package com.dms.dailyjoy.data.repository

import com.dms.dailyjoy.data.database.dao.PleasureDao
import com.dms.dailyjoy.data.database.entity.PleasureHistoryEntry
import com.dms.dailyjoy.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val pleasureDao: PleasureDao
) : HistoryRepository {
    override fun getHistoryForDateRange(startDate: Long, endDate: Long): Flow<List<PleasureHistoryEntry>> {
        return pleasureDao.getHistoryForDateRange(startDate, endDate)
    }
}