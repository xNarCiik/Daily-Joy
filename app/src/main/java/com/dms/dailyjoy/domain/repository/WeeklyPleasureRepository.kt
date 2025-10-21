package com.dms.dailyjoy.domain.repository

import com.dms.dailyjoy.data.database.entity.WeeklyPleasureEntity
import kotlinx.coroutines.flow.Flow

interface WeeklyPleasureRepository {
    fun getWeeklyPleasures(weekId: Int): Flow<List<WeeklyPleasureEntity>>
    suspend fun saveWeeklyPleasures(pleasures: List<WeeklyPleasureEntity>)
}
