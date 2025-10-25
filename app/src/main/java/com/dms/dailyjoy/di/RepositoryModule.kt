package com.dms.dailyjoy.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.dms.dailyjoy.data.database.dao.PleasureDao
import com.dms.dailyjoy.data.local.LocalDailyMessagesDataSource
import com.dms.dailyjoy.data.repository.DailyMessageRepositoryImpl
import com.dms.dailyjoy.data.repository.DailyPleasureRepositoryImpl
import com.dms.dailyjoy.data.repository.HistoryRepositoryImpl
import com.dms.dailyjoy.data.repository.PleasureRepositoryImpl
import com.dms.dailyjoy.data.repository.SettingsRepositoryImpl
import com.dms.dailyjoy.data.repository.SocialRepositoryImpl
import com.dms.dailyjoy.data.repository.StatisticsRepositoryImpl
import com.dms.dailyjoy.domain.repository.DailyMessageRepository
import com.dms.dailyjoy.domain.repository.DailyPleasureRepository
import com.dms.dailyjoy.domain.repository.HistoryRepository
import com.dms.dailyjoy.domain.repository.PleasureRepository
import com.dms.dailyjoy.domain.repository.SettingsRepository
import com.dms.dailyjoy.domain.repository.SocialRepository
import com.dms.dailyjoy.domain.repository.StatisticsRepository
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
    fun bindPleasureRepository(pleasureDao: PleasureDao): PleasureRepository =
        PleasureRepositoryImpl(pleasureDao = pleasureDao)

    @Provides
    @Singleton
    fun bindDailyPleasureRepository(
        dataStore: DataStore<Preferences>,
        pleasureRepository: PleasureRepository
    ): DailyPleasureRepository =
        DailyPleasureRepositoryImpl(dataStore = dataStore, pleasureRepository = pleasureRepository)

    @Provides
    @Singleton
    fun provideHistoryRepository(pleasureDao: PleasureDao): HistoryRepository =
        HistoryRepositoryImpl(pleasureDao)

    @Provides
    @Singleton
    fun provideDailyMessageRepository(localDataSource: LocalDailyMessagesDataSource): DailyMessageRepository =
        DailyMessageRepositoryImpl(localDataSource = localDataSource)

    @Provides
    @Singleton
    fun provideSettingsRepository(dataStore: DataStore<Preferences>): SettingsRepository =
        SettingsRepositoryImpl(dataStore = dataStore)

    @Provides
    @Singleton
    fun provideSocialRepository(): SocialRepository = SocialRepositoryImpl()

    @Provides
    @Singleton
    fun provideStatisticsRepository(): StatisticsRepository = StatisticsRepositoryImpl()
}
