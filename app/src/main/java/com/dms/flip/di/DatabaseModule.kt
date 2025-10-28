package com.dms.flip.di

import android.content.Context
import androidx.room.Room
import com.dms.flip.data.database.AppDatabase
import com.dms.flip.data.database.DatabaseCallback
import com.dms.flip.data.database.dao.PleasureDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        databaseCallback: DatabaseCallback
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "daily_joy_database"
    ).addCallback(databaseCallback).build()

    @Provides
    fun providePleasureDao(appDatabase: AppDatabase): PleasureDao = appDatabase.pleasureDao()
}
