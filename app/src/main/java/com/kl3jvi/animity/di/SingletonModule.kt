package com.kl3jvi.animity.di

import com.kl3jvi.animity.data.repository.LoginRepositoryImpl
import com.kl3jvi.animity.data.repository.PersistenceRepositoryImpl
import com.kl3jvi.animity.domain.repositories.LoginRepository
import com.kl3jvi.animity.domain.repositories.PersistenceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SingletonModule {
    @Binds
    abstract fun bindPersistenceRepository(repository: PersistenceRepositoryImpl): PersistenceRepository

    @Binds
    abstract fun bindLoginRepository(repository: LoginRepositoryImpl): LoginRepository
}
