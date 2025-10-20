package com.dms.dailyjoy.domain.repository

import com.dms.dailyjoy.data.model.Pleasure
import kotlinx.coroutines.flow.Flow

interface PleasureRepository {
    fun getAllPleasures(): Flow<List<Pleasure>>
    suspend fun getPleasureById(id: Int): Pleasure?
    suspend fun insert(pleasure: Pleasure)
    suspend fun delete(pleasure: Pleasure)
}
