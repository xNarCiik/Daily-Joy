package com.dms.dailyjoy.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dms.dailyjoy.data.database.entity.PleasureHistoryEntity
import java.time.LocalDate

@Dao
interface PleasureHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(dailyPleasure: PleasureHistoryEntity)

    @Query("SELECT * FROM pleasure_history WHERE date = :date")
    suspend fun getForDate(date: LocalDate): PleasureHistoryEntity?
}
