package com.dms.dailyjoy.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dms.dailyjoy.data.database.entity.WeeklyPleasureEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeeklyPleasureDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(weeklyPleasures: List<WeeklyPleasureEntity>)

    @Query("SELECT * FROM weekly_pleasure WHERE weekId = :weekId")
    fun getWeeklyPleasures(weekId: Int): Flow<List<WeeklyPleasureEntity>>

    @Query("DELETE FROM weekly_pleasure WHERE weekId = :weekId")
    suspend fun deleteWeeklyPleasures(weekId: Int)
}
