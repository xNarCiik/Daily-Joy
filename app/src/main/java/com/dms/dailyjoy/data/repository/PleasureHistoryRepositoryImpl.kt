package com.dms.dailyjoy.data.repository

import com.dms.dailyjoy.data.database.dao.PleasureHistoryDao
import com.dms.dailyjoy.data.database.mapper.toHistoryEntity
import com.dms.dailyjoy.data.database.mapper.toPleasure
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.domain.repository.PleasureHistoryRepository
import java.time.LocalDate
import javax.inject.Inject

class PleasureHistoryRepositoryImpl @Inject constructor(private val pleasureHistoryDao: PleasureHistoryDao) :
    PleasureHistoryRepository {

    override suspend fun getForDate(date: LocalDate): Pleasure? =
        pleasureHistoryDao.getForDate(date)?.toPleasure()

    override suspend fun save(pleasure: Pleasure) =
        pleasureHistoryDao.save(pleasure.toHistoryEntity())
}
