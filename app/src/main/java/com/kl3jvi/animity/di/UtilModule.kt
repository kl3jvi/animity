package com.kl3jvi.animity.di

import android.content.Context
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.kl3jvi.animity.utils.Constants.Companion.getDataSourceFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object UtilModule {

    @Singleton
    @Provides
    fun provideExoPlayerDatabase(@ApplicationContext context: Context): StandaloneDatabaseProvider {
        return StandaloneDatabaseProvider(context)
    }

    @Singleton
    @Provides
    fun provideDownloadContentDirectory(
        @ApplicationContext context: Context
    ): File {
        return File(context.getExternalFilesDir(null), "Animity")
    }

    @Singleton
    @Provides
    fun provideDownloadCache(
        downloadContentDirectory: File,
        databaseProvider: StandaloneDatabaseProvider
    ): SimpleCache {
        return SimpleCache(downloadContentDirectory, NoOpCacheEvictor(), databaseProvider)
    }

    @Singleton
    @Provides
    fun provideDownloadManager(
        @ApplicationContext context: Context,
        databaseProvider: StandaloneDatabaseProvider,
        downloadCache: SimpleCache
    ): DownloadManager {
        return DownloadManager(
            context, databaseProvider, downloadCache, getDataSourceFactory(), Runnable::run
        )
    }
}
