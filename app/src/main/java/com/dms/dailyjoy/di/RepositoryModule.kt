package com.dms.dailyjoy.di

import com.dms.dailyjoy.data.database.dao.PleasureDao
import com.dms.dailyjoy.data.database.dao.PleasureHistoryDao
import com.dms.dailyjoy.data.repository.DailyMessageRepositoryImpl
import com.dms.dailyjoy.data.repository.PleasureHistoryRepositoryImpl
import com.dms.dailyjoy.data.repository.PleasureRepositoryImpl
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
    fun bindPleasureRepository(pleasureDao: PleasureDao): PleasureRepository =
        PleasureRepositoryImpl(pleasureDao = pleasureDao)

    @Provides
    @Singleton
    fun bindPleasureHistoryRepository(pleasureHistoryDao: PleasureHistoryDao): PleasureHistoryRepository =
        PleasureHistoryRepositoryImpl(pleasureHistoryDao = pleasureHistoryDao)

    @Provides
    @Singleton
    fun provideDailyMessageRepository(): DailyMessageRepository = DailyMessageRepositoryImpl()
}
