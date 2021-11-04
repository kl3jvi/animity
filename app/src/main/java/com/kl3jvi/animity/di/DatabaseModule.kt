package com.kl3jvi.animity.di

import android.content.Context
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.kl3jvi.animity.model.database.AnimeDao
import com.kl3jvi.animity.model.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideAnimesDao(appDatabase: AppDatabase): AnimeDao {
        return appDatabase.animeDao()
    }

    @Singleton
    @Provides
    fun provideExoPlayerDatabase(@ApplicationContext context: Context): ExoDatabaseProvider {
        return ExoDatabaseProvider(context)
    }


}