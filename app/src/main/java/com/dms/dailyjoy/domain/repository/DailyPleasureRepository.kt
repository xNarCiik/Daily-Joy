package com.dms.dailyjoy.domain.repository

import com.dms.dailyjoy.data.model.Pleasure
import kotlinx.coroutines.flow.Flow

interface DailyPleasureRepository {

    fun getDailyPleasure(): Flow<Pleasure?>

    suspend fun saveDailyPleasure(pleasure: Pleasure)
}
