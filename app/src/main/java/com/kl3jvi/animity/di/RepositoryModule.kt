package com.kl3jvi.animity.di

import com.kl3jvi.animity.data.repository.*
import com.kl3jvi.animity.domain.repositories.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
@InstallIn(SingletonComponent::class)
@ExperimentalCoroutinesApi
abstract class RepositoryModule {

    @Binds
    abstract fun bindFavoritesRepository(repository: FavoriteRepositoryImpl): FavoriteRepository

    @Binds
    abstract fun bindHomeRepository(repository: HomeRepositoryImpl): HomeRepository

    @Binds
    abstract fun bindSearchRepository(repository: SearchRepositoryImpl): SearchRepository

    @Binds
    abstract fun bindPlayerRepository(repository: PlayerRepositoryImpl): PlayerRepository

    @Binds
    abstract fun bindUserRepository(repository: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindDetailsRepository(repository: DetailsRepositoryImpl): DetailsRepository

    @Binds
    abstract fun bindLoginRepository(repository: LoginRepositoryImpl): LoginRepository

    @Binds
    abstract fun bindProfileRepository(repository: ProfileRepositoryImpl): ProfileRepository

    @Binds
    abstract fun bindPersistenceRepository(repository: PersistenceRepositoryImpl): PersistenceRepository

    @Binds
    abstract fun bindFirebaseRepository(repository: FirebaseRemoteConfigRepositoryImpl): FirebaseRemoteConfigRepository
}
