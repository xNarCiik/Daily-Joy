package com.dms.dailyjoy.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dms.dailyjoy.data.database.entity.PleasureEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PleasureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pleasure: PleasureEntity)

    @Delete
    suspend fun delete(pleasure: PleasureEntity)

    @Query("SELECT * FROM pleasures")
    fun getAllPleasures(): Flow<List<PleasureEntity>>

    @Query("SELECT * FROM pleasures WHERE id = :id")
    suspend fun getPleasureById(id: Int): PleasureEntity?
}
