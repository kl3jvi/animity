package com.kl3jvi.animity.di

import com.kl3jvi.animity.network.AnimeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Singleton
    @Provides
    fun provideAnimeService(): AnimeService {
        return AnimeService.create()
    }
}
