package com.kl3jvi.animity.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.kl3jvi.animity.data.repository.DataStoreManagerImpl
import com.kl3jvi.animity.domain.repositories.persistence_repositories.DataStoreManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    private val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore(name = "animity")

    @Singleton
    @Provides
    fun providePreferencesDataStore(
        @ApplicationContext app: Context
    ): DataStore<Preferences> = app.tokenDataStore

}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreManagerModule {

    @Singleton
    @Binds
    abstract fun bindDataStoreService(impl: DataStoreManagerImpl): DataStoreManager
}