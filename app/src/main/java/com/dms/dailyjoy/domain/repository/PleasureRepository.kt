package com.dms.dailyjoy.domain.repository

import com.dms.dailyjoy.data.model.Pleasure

interface PleasureRepository {
    suspend fun getAllPleasures(): List<Pleasure>
    suspend fun getPleasureById(id: Int): Pleasure?
    suspend fun insert(pleasure: Pleasure)
    suspend fun delete(pleasure: Pleasure)
}
