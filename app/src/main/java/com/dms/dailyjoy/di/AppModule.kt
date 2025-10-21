package com.dms.dailyjoy.di

import android.app.Application
import android.content.res.Resources
import com.dms.dailyjoy.domain.repository.DailyMessageRepository
import com.dms.dailyjoy.domain.repository.PleasureRepository
import com.dms.dailyjoy.domain.usecase.GetPleasuresUseCase
import com.dms.dailyjoy.domain.usecase.GetRandomDailyMessageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideGetRandomDailyMessageUseCase(repository: DailyMessageRepository) =
        GetRandomDailyMessageUseCase(repository = repository)

    @Provides
    @Singleton
    fun provideGetPleasuresUseCase(repository: PleasureRepository) =
        GetPleasuresUseCase(repository = repository)

    @Provides
    @Singleton
    fun provideResources(application: Application): Resources = application.resources
}
