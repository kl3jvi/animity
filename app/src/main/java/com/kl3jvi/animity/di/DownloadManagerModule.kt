package com.kl3jvi.animity.di

import android.content.Context
import com.google.android.exoplayer2.database.DefaultDatabaseProvider
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.offline.DefaultDownloadIndex
import com.google.android.exoplayer2.offline.DefaultDownloaderFactory
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
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
object DownloadManagerModule {

    @Provides
    @Singleton
    fun provideExoDownloadManager(
        @ApplicationContext context: Context,
        simpleCache: SimpleCache,
    ): DownloadManager {
        val databaseProvider = DefaultDatabaseProvider(StandaloneDatabaseProvider(context))

        val upstreamFactory = DefaultHttpDataSource.Factory()
            .setUserAgent("Your User Agent")

        val cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(simpleCache)
            .setUpstreamDataSourceFactory(upstreamFactory)

        return DownloadManager(
            context,
            DefaultDownloadIndex(databaseProvider),
            DefaultDownloaderFactory(cacheDataSourceFactory, Executors.newFixedThreadPool(6)),
        )
    }

    @Provides
    @Singleton
    fun provideSimpleCache(
        @ApplicationContext context: Context,
    ): SimpleCache {
        val databaseProvider = StandaloneDatabaseProvider(context)
        return SimpleCache(
            File(
                context.cacheDir,
                "exoplayer",
            ), // Ensures always fresh file
            LeastRecentlyUsedCacheEvictor(300L * 1024L * 1024L),
            databaseProvider,
        )
    }
}
