package com.kl3jvi.animity.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {
    /**
     * Provide an IO dispatcher for coroutines.
     *
     * @return A CoroutineDispatcher
     */
    @Provides
    @Singleton
    fun provideIODispatcher() = Dispatchers.IO

}