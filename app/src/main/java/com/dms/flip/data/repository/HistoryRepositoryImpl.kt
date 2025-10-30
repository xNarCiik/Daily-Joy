package com.dms.flip.data.repository

import com.dms.flip.domain.model.PleasureHistory
import com.dms.flip.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/*
class HistoryRepositoryImpl @Inject constructor(
    private val pleasureDao: PleasureDao
) : HistoryRepository {
    override fun getHistoryForDateRange(startDate: Long, endDate: Long): Flow<List<PleasureHistoryEntry>> {
        return pleasureDao.getHistoryForDateRange(startDate, endDate)
    }
}*/

class HistoryRepositoryImpl @Inject constructor() : HistoryRepository {
    override fun getHistoryForDateRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<PleasureHistory>> {
        return flowOf(listOf()) // TODO
    }
}
