package com.kl3jvi.animity.di

import com.kl3jvi.animity.data.network.anime_service.AnimeApiClient
import com.kl3jvi.animity.data.repository.activity_repositories.PlayerRepositoryImpl
import com.kl3jvi.animity.data.repository.fragment_repositories.DetailsRepositoryImpl
import com.kl3jvi.animity.data.repository.fragment_repositories.FavoriteRepositoryImpl
import com.kl3jvi.animity.data.repository.fragment_repositories.HomeRepositoryImpl
import com.kl3jvi.animity.data.repository.fragment_repositories.SearchRepositoryImpl
import com.kl3jvi.animity.domain.repositories.activity_repositories.PlayerRepository
import com.kl3jvi.animity.domain.repositories.fragment_repositories.DetailsRepository
import com.kl3jvi.animity.domain.repositories.fragment_repositories.FavoriteRepository
import com.kl3jvi.animity.domain.repositories.fragment_repositories.HomeRepository
import com.kl3jvi.animity.domain.repositories.fragment_repositories.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @Provides
    @ViewModelScoped
    fun provideDetailsRepository(
        apiClient: AnimeApiClient,
        ioDispatcher: CoroutineDispatcher
    ): DetailsRepository {
        return DetailsRepositoryImpl(apiClient, ioDispatcher)
    }

    @Provides
    @ViewModelScoped
    fun provideHomeRepository(
        apiClient: AnimeApiClient,
        ioDispatcher: CoroutineDispatcher
    ): HomeRepository {
        return HomeRepositoryImpl(apiClient, ioDispatcher)
    }

    @Provides
    @ViewModelScoped
    fun provideSearchRepository(
        apiClient: AnimeApiClient,
        ioDispatcher: CoroutineDispatcher
    ): SearchRepository {
        return SearchRepositoryImpl(apiClient, ioDispatcher)
    }

    @Provides
    @ViewModelScoped
    fun providePlayerRepository(
        apiClient: AnimeApiClient,
        ioDispatcher: CoroutineDispatcher
    ): PlayerRepository {
        return PlayerRepositoryImpl(apiClient, ioDispatcher)
    }

    @Provides
    @ViewModelScoped
    fun provideFavoritesRepository(
        apiClient: AnimeApiClient,
        ioDispatcher: CoroutineDispatcher
    ): FavoriteRepository {
        return FavoriteRepositoryImpl(apiClient, ioDispatcher)
    }

}