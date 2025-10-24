package com.dms.dailyjoy.di

import android.app.Application
import android.content.res.Resources
import com.dms.dailyjoy.domain.repository.DailyMessageRepository
import com.dms.dailyjoy.domain.repository.PleasureRepository
import com.dms.dailyjoy.domain.usecase.GetRandomDailyMessageUseCase
import com.dms.dailyjoy.domain.usecase.dailypleasure.DrawDailyPleasureUseCase
import com.dms.dailyjoy.domain.usecase.pleasures.GetPleasuresUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @ApplicationScope
    @Singleton
    @Provides
    fun provideApplicationScope(): CoroutineScope = CoroutineScope(SupervisorJob())

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
    fun provideDrawDailyPleasureUseCase(repository: PleasureRepository) =
        DrawDailyPleasureUseCase(repository = repository)

    @Provides
    @Singleton
    fun provideResources(application: Application): Resources = application.resources
}