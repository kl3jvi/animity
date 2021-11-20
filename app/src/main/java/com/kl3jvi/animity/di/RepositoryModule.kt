package com.kl3jvi.animity.di

import com.kl3jvi.animity.network.AnimeService
import com.kl3jvi.animity.repository.DetailsRepository
import com.kl3jvi.animity.repository.HomeRepository
import com.kl3jvi.animity.repository.PlayerRepository
import com.kl3jvi.animity.repository.SearchRepository
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
        apiHelper: AnimeService
    ): DetailsRepository {
        return DetailsRepository(apiHelper)
    }

    @Provides
    @ViewModelScoped
    fun provideHomeRepository(
        apiHelper: AnimeService
    ): HomeRepository {
        return HomeRepository(apiHelper)
    }

    @Provides
    @ViewModelScoped
    fun provideSearchRepository(
        apiHelper: AnimeService
    ): SearchRepository {
        return SearchRepository(apiHelper)
    }

    @Provides
    @ViewModelScoped
    fun providePlayerRepository(
        apiHelper: AnimeService
    ): PlayerRepository {
        return PlayerRepository(apiHelper)
    }

}