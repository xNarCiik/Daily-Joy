package com.dms.dailyjoy.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dms.dailyjoy.data.database.dao.PleasureDao
import com.dms.dailyjoy.data.database.entity.PleasureEntity
import com.dms.dailyjoy.data.database.entity.PleasureHistoryEntry

@Database(entities = [PleasureEntity::class, PleasureHistoryEntry::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pleasureDao(): PleasureDao
}
