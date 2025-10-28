package com.dms.flip.data.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dms.flip.data.database.dao.PleasureDao
import com.dms.flip.data.database.mapper.toEntity
import com.dms.flip.data.local.LocalPleasureDataSource
import com.dms.flip.di.ApplicationScope
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
