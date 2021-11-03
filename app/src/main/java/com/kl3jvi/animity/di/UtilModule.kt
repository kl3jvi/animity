package com.kl3jvi.animity.di

import android.content.Context
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File

@InstallIn(SingletonComponent::class)
@Module
class UtilModule {

    @Provides
    fun provideCache(
        downloadContentDirectory: File,
        exoPlayerDatabase: ExoDatabaseProvider
    ): Cache {
        return SimpleCache(downloadContentDirectory, NoOpCacheEvictor(), exoPlayerDatabase)
    }

    @Provides
    fun provideDownloadDirectory(@ApplicationContext context: Context): File {
        return File(context.getExternalFilesDir(null), "Animity")
    }

    @Provides
    fun provideDownloadManager(
        @ApplicationContext context: Context,
        exoPlayerDatabase: ExoDatabaseProvider,
        cache: Cache,
        dataSource: DefaultHttpDataSource.Factory
    ): DownloadManager {
        return DownloadManager(context, exoPlayerDatabase, cache, dataSource, Runnable::run)
    }

}