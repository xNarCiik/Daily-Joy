package com.dms.dailyjoy.di

import android.app.Application
import com.dms.dailyjoy.data.database.dao.PleasureDao
import com.dms.dailyjoy.data.database.dao.PleasureHistoryDao
import com.dms.dailyjoy.data.local.LocalDailyMessagesDataSource
import com.dms.dailyjoy.data.local.LocalPleasureDataSource
import com.dms.dailyjoy.data.repository.DailyMessageRepositoryImpl
import com.dms.dailyjoy.data.repository.PleasureHistoryRepositoryImpl
import com.dms.dailyjoy.data.repository.PleasureRepositoryImpl
import com.dms.dailyjoy.data.repository.SettingsRepository
import com.dms.dailyjoy.data.repository.SettingsRepositoryImpl
import com.dms.dailyjoy.domain.repository.DailyMessageRepository
import com.dms.dailyjoy.domain.repository.PleasureHistoryRepository
import com.dms.dailyjoy.domain.repository.PleasureRepository
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
    fun bindPleasureHistoryRepository(pleasureHistoryDao: PleasureHistoryDao): PleasureHistoryRepository =
        PleasureHistoryRepositoryImpl(pleasureHistoryDao = pleasureHistoryDao)

    @Provides
    @Singleton
    fun provideDailyMessageRepository(localDataSource: LocalDailyMessagesDataSource): DailyMessageRepository =
        DailyMessageRepositoryImpl(localDataSource = localDataSource)

    @Provides
    @Singleton
    fun provideSettingsRepository(context: Application): SettingsRepository =
        SettingsRepositoryImpl(context = context)
}
