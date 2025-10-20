package com.dms.dailyjoy.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dms.dailyjoy.data.database.converter.DateConverter
import com.dms.dailyjoy.data.database.dao.PleasureDao
import com.dms.dailyjoy.data.database.dao.PleasureHistoryDao
import com.dms.dailyjoy.data.database.entity.PleasureEntity
import com.dms.dailyjoy.data.database.entity.PleasureHistoryEntity

@Database(
    entities = [PleasureEntity::class, PleasureHistoryEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pleasureDao(): PleasureDao
    abstract fun pleasureHistoryDao(): PleasureHistoryDao
}
