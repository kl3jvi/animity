package com.kl3jvi.animity.di

import android.content.Context
import com.kl3jvi.animity.network.AnimeService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Singleton
    @Provides
    fun provideAnimeService(@ApplicationContext context: Context): AnimeService {
        return AnimeService.create(context)
    }
}
