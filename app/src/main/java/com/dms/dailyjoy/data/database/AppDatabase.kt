package com.dms.dailyjoy.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dms.dailyjoy.data.database.dao.PleasureDao
import com.dms.dailyjoy.data.database.entity.PleasureEntity

@Database(entities = [PleasureEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pleasureDao(): PleasureDao
}
