package com.dms.dailyjoy.domain.repository

import com.dms.dailyjoy.data.database.entity.PleasureHistoryEntry
import com.dms.dailyjoy.data.model.Pleasure
import com.dms.dailyjoy.data.model.PleasureCategory
import kotlinx.coroutines.flow.Flow

interface PleasureRepository {
    fun getAllPleasures(): Flow<List<Pleasure>>
    fun getPleasuresCount(): Flow<Int>
    fun getRandomPleasure(category: PleasureCategory?): Flow<Pleasure>
    suspend fun insert(pleasure: Pleasure)
    suspend fun update(pleasure: Pleasure)
    suspend fun delete(pleasure: Pleasure)
    suspend fun upsertHistoryEntry(entry: PleasureHistoryEntry)
    suspend fun getHistoryEntryForDay(dayIdentifier: String): PleasureHistoryEntry?
}
