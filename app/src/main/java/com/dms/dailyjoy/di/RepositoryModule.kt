package com.dms.dailyjoy.di

import android.app.Application
import com.dms.dailyjoy.data.database.dao.PleasureDao
import com.dms.dailyjoy.data.database.dao.WeeklyPleasureDao
import com.dms.dailyjoy.data.local.LocalDailyMessagesDataSource
import com.dms.dailyjoy.data.local.LocalPleasureDataSource
import com.dms.dailyjoy.data.repository.DailyMessageRepositoryImpl
import com.dms.dailyjoy.data.repository.PleasureRepositoryImpl
import com.dms.dailyjoy.data.repository.SettingsRepositoryImpl
import com.dms.dailyjoy.data.repository.WeeklyPleasureRepositoryImpl
import com.dms.dailyjoy.domain.repository.DailyMessageRepository
import com.dms.dailyjoy.domain.repository.PleasureRepository
import com.dms.dailyjoy.domain.repository.SettingsRepository
import com.dms.dailyjoy.domain.repository.WeeklyPleasureRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun bindPleasureRepository(
        pleasureDao: PleasureDao,
        localDataSource: LocalPleasureDataSource
    ): PleasureRepository =
        PleasureRepositoryImpl(pleasureDao = pleasureDao, localDataSource = localDataSource)

    @Provides
    @Singleton
    fun provideWeeklyPleasureRepository(weeklyPleasureDao: WeeklyPleasureDao): WeeklyPleasureRepository =
        WeeklyPleasureRepositoryImpl(weeklyPleasureDao)

    @Provides
    @Singleton
    fun provideDailyMessageRepository(localDataSource: LocalDailyMessagesDataSource): DailyMessageRepository =
        DailyMessageRepositoryImpl(localDataSource = localDataSource)

    @Provides
    @Singleton
    fun provideSettingsRepository(context: Application): SettingsRepository =
        SettingsRepositoryImpl(context = context)
}
