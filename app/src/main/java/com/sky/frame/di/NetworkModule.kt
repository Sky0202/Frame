package com.sky.frame.di

import com.sky.frame.api.FreshService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideKanbanServiceService(): FreshService {
        return FreshService.create()
    }
}
