package com.dms.flip.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dms.flip.data.database.dao.PleasureDao
import com.dms.flip.data.database.entity.PleasureEntity
import com.dms.flip.data.database.entity.PleasureHistoryEntry

@Database(entities = [PleasureEntity::class, PleasureHistoryEntry::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pleasureDao(): PleasureDao
}
