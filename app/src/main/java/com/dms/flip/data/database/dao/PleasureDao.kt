package com.dms.flip.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.dms.flip.data.database.entity.PleasureEntity
import com.dms.flip.data.database.entity.PleasureHistoryEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface PleasureDao {
    @Query("SELECT * FROM pleasure")
    fun getAllPleasures(): Flow<List<PleasureEntity>>

    @Query("SELECT * FROM pleasure WHERE id = :id")
    suspend fun getPleasureById(id: Int): PleasureEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pleasure: PleasureEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pleasures: List<PleasureEntity>)

    @Update
    suspend fun update(pleasure: PleasureEntity)

    @Delete
    suspend fun delete(pleasure: PleasureEntity)

    @Upsert
    suspend fun upsertHistoryEntry(entry: PleasureHistoryEntry)

    @Query("SELECT * FROM pleasure_history WHERE dateDrawn BETWEEN :startDate AND :endDate ORDER BY dateDrawn DESC")
    fun getHistoryForDateRange(startDate: Long, endDate: Long): Flow<List<PleasureHistoryEntry>>

    @Query("SELECT * FROM pleasure_history WHERE dayIdentifier = :dayIdentifier")
    suspend fun getHistoryEntryForDay(dayIdentifier: String): PleasureHistoryEntry?
}
