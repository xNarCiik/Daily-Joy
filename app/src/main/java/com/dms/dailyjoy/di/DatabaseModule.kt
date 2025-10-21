package com.dms.dailyjoy.di

import android.content.Context
import androidx.room.Room
import com.dms.dailyjoy.data.database.AppDatabase
import com.dms.dailyjoy.data.database.DatabaseCallback
import com.dms.dailyjoy.data.database.dao.PleasureDao
import com.dms.dailyjoy.data.database.dao.WeeklyPleasureDao
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

    @Provides
    fun provideWeeklyPleasureDao(appDatabase: AppDatabase): WeeklyPleasureDao =
        appDatabase.weeklyPleasureDao()
}
