package com.dms.dailyjoy.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dms.dailyjoy.data.database.entity.PleasureEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PleasureDao {
    @Query("SELECT * FROM pleasure")
    fun getAllPleasures(): Flow<List<PleasureEntity>>

    @Query("SELECT * FROM pleasure WHERE id = :id")
    suspend fun getPleasureById(id: Int): PleasureEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pleasure: PleasureEntity)

    @Update
    suspend fun update(pleasure: PleasureEntity)

    @Delete
    suspend fun delete(pleasure: PleasureEntity)
}
