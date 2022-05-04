package com.kl3jvi.animity.di

import com.kl3jvi.animity.data.repository.activity_repositories.LoginRepositoryImpl
import com.kl3jvi.animity.data.repository.activity_repositories.PlayerRepositoryImpl
import com.kl3jvi.animity.data.repository.fragment_repositories.*
import com.kl3jvi.animity.data.repository.persistence_repository.LocalStorageImpl
import com.kl3jvi.animity.data.repository.persistence_repository.PersistenceRepositoryImpl
import com.kl3jvi.animity.domain.repositories.activity_repositories.LoginRepository
import com.kl3jvi.animity.domain.repositories.activity_repositories.PlayerRepository
import com.kl3jvi.animity.domain.repositories.fragment_repositories.*
import com.kl3jvi.animity.domain.repositories.persistence_repositories.LocalStorage
import com.kl3jvi.animity.domain.repositories.persistence_repositories.PersistenceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Module
@InstallIn(SingletonComponent::class)
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
    abstract fun bindLocalRepository(repository: LocalStorageImpl): LocalStorage

    @Binds
    abstract fun bindPersistenceRepository(repository: PersistenceRepositoryImpl): PersistenceRepository
}
