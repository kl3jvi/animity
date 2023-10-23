@file:Suppress("DEPRECATION")

package com.kl3jvi.animity.di

import android.content.Context
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.kl3jvi.animity.data.downloader.Downloader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExoPlayerModule {
    @Provides
    @Singleton
    fun provideDownloadCache(
        @ApplicationContext context: Context,
        databaseProvider: ExoDatabaseProvider,
    ): SimpleCache {
        val cacheDirectory = File(context.filesDir, "downloads")
        return SimpleCache(cacheDirectory, NoOpCacheEvictor(), databaseProvider)
    }

    @Provides
    @Singleton
    fun provideDatabaseProvider(
        @ApplicationContext context: Context,
    ): ExoDatabaseProvider {
        return ExoDatabaseProvider(context)
    }

    @Provides
    @Singleton
    fun provideDownloadManager(
        @ApplicationContext context: Context,
        downloadCache: SimpleCache,
        databaseProvider: ExoDatabaseProvider,
    ): DownloadManager {
        return DownloadManager(
            context,
            databaseProvider,
            downloadCache,
            DefaultHttpDataSource.Factory(),
            Executors.newFixedThreadPool(6),
        )
    }

    @Provides
    @Singleton
    fun provideDownloader(
        @ApplicationContext context: Context,
        downloadManager: DownloadManager,
    ): Downloader {
        return Downloader(
            appContext = context,
            downloadManager = downloadManager,
        )
    }
}
