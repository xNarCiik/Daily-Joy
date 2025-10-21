package com.dms.dailyjoy.data.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dms.dailyjoy.data.database.dao.PleasureDao
import com.dms.dailyjoy.data.database.mapper.toEntity
import com.dms.dailyjoy.data.local.LocalPleasureDataSource
import com.dms.dailyjoy.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class DatabaseCallback @Inject constructor(
    private val pleasureDaoProvider: Provider<PleasureDao>,
    private val localPleasureDataSource: LocalPleasureDataSource,
    @ApplicationScope private val applicationScope: CoroutineScope
) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        applicationScope.launch {
            val pleasures = localPleasureDataSource.getPleasures().map { it.toEntity() }
            pleasureDaoProvider.get().insertAll(pleasures)
        }
    }
}
