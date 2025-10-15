package com.dms.dailyjoy.di

import com.dms.dailyjoy.data.repository.PleasureRepositoryImpl
import com.dms.dailyjoy.domain.repository.PleasureRepository
import com.dms.dailyjoy.domain.usecase.GetPleasuresUseCase
import com.dms.dailyjoy.domain.usecase.GetRandomPleasureUseCase
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
    fun providePleasureRepository(): PleasureRepository = PleasureRepositoryImpl()

    @Provides
    @Singleton
    fun provideGetPleasuresUseCase(repository: PleasureRepository) =
        GetPleasuresUseCase(repository = repository)

    @Provides
    @Singleton
    fun provideGetRandomPleasureUseCase(repository: PleasureRepository) =
        GetRandomPleasureUseCase(repository = repository)
}