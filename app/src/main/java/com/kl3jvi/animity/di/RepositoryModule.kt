package com.kl3jvi.animity.di

import com.kl3jvi.animity.data.network.AnimeApiClient
import com.kl3jvi.animity.data.repository.DetailsRepositoryImpl
import com.kl3jvi.animity.data.repository.HomeRepositoryImpl
import com.kl3jvi.animity.data.repository.PlayerRepositoryImpl
import com.kl3jvi.animity.data.repository.SearchRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @Provides
    @ViewModelScoped
    fun provideDetailsRepository(
        apiClient: AnimeApiClient
    ): DetailsRepositoryImpl {
        return DetailsRepositoryImpl(apiClient)
    }

    @Provides
    @ViewModelScoped
    fun provideHomeRepository(
        apiClient: AnimeApiClient
    ): HomeRepositoryImpl {
        return HomeRepositoryImpl(apiClient)
    }

    @Provides
    @ViewModelScoped
    fun provideSearchRepository(
        apiClient: AnimeApiClient
    ): SearchRepositoryImpl {
        return SearchRepositoryImpl(apiClient)
    }

    @Provides
    @ViewModelScoped
    fun providePlayerRepository(
        apiClient: AnimeApiClient
    ): PlayerRepositoryImpl {
        return PlayerRepositoryImpl(apiClient)
    }

}