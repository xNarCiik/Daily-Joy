package com.dms.dailyjoy.data.repository

import com.dms.dailyjoy.data.database.dao.WeeklyPleasureDao
import com.dms.dailyjoy.data.database.entity.WeeklyPleasureEntity
import com.dms.dailyjoy.domain.repository.WeeklyPleasureRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeeklyPleasureRepositoryImpl @Inject constructor(
    private val weeklyPleasureDao: WeeklyPleasureDao
) : WeeklyPleasureRepository {

    override fun getWeeklyPleasures(weekId: Int): Flow<List<WeeklyPleasureEntity>> {
        return weeklyPleasureDao.getWeeklyPleasures(weekId)
    }

    override suspend fun saveWeeklyPleasures(pleasures: List<WeeklyPleasureEntity>) {
        weeklyPleasureDao.insertAll(pleasures)
    }
}
