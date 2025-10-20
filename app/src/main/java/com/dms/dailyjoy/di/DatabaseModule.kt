package com.dms.dailyjoy.di

import android.content.Context
import androidx.room.Room
import com.dms.dailyjoy.data.database.AppDatabase
import com.dms.dailyjoy.data.database.dao.PleasureDao
import com.dms.dailyjoy.data.database.dao.PleasureHistoryDao
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
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "daily_joy_database"
        ).build()
    }

    @Provides
    fun providePleasureDao(appDatabase: AppDatabase): PleasureDao = appDatabase.pleasureDao()

    @Provides
    fun providePleasureHistoryDao(appDatabase: AppDatabase): PleasureHistoryDao =
        appDatabase.pleasureHistoryDao()
}
