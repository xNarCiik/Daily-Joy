package com.dms.dailyjoy.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dms.dailyjoy.data.database.entity.PleasureEntity

@Dao
interface PleasureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pleasure: PleasureEntity)

    @Delete
    suspend fun delete(pleasure: PleasureEntity)

    @Query("SELECT * FROM pleasure")
    suspend fun getAllPleasures(): List<PleasureEntity>

    @Query("SELECT * FROM pleasure WHERE id = :id")
    suspend fun getPleasureById(id: Int): PleasureEntity?
}
