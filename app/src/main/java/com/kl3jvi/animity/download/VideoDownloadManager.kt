package com.kl3jvi.animity.download

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.DefaultDatabaseProvider
import com.google.android.exoplayer2.offline.DefaultDownloadIndex
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadIndex
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadRequest
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.io.File
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoDownloadManager @Inject constructor(
    private val context: Context,
) {
    companion object {
        private const val DOWNLOAD_NOTIFICATION_CHANNEL_ID = "download_channel"
        private const val DOWNLOAD_CONTENT_DIRECTORY = "downloads"
    }

    private val downloadManager: DownloadManager
    private val downloadIndex: DownloadIndex
    private val downloadCache: Cache
    private val databaseProvider: DatabaseProvider

    init {
        // Create a database provider.
        val databaseOpenHelper: SQLiteOpenHelper =
            object : SQLiteOpenHelper(context, "exo_database", null, 1) {
                override fun onCreate(db: SQLiteDatabase) {}
                override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
            }

        databaseProvider = DefaultDatabaseProvider(databaseOpenHelper)

        // Create downloads directory.
        val downloadDirectory = context.getExternalFilesDir(null)
            ?.let { File(it, DOWNLOAD_CONTENT_DIRECTORY) }
            ?: throw IllegalStateException("Could not create download directory")

        // Create cache.
        downloadCache = SimpleCache(
            downloadDirectory,
            LeastRecentlyUsedCacheEvictor(100 * 1024 * 1024 /* 100MB */),
            databaseProvider,
        )

        // Create download index.
        downloadIndex = DefaultDownloadIndex(databaseProvider)

        // Create download manager.
        downloadManager = DownloadManager(
            context,
            databaseProvider,
            downloadCache,
            DefaultHttpDataSource.Factory(),
            Executors.newFixedThreadPool(6),
        )
    }

    /**
     * Starts downloading the video from the specified URL.
     */
    fun startDownload(url: String) {
        val downloadRequest = DownloadRequest.Builder("contentId", Uri.parse(url)).build()

        downloadManager.addDownload(downloadRequest)
    }

    /**
     * Cancels the download with the specified id.
     */
    fun cancelDownload(id: String) {
        downloadManager.removeDownload(id)
    }

    /**
     * Returns a LiveData object that holds the download state.
     */
    fun getDownloadState(url: String) = callbackFlow {
        val listener = object : DownloadManager.Listener {
            override fun onDownloadChanged(
                downloadManager: DownloadManager,
                download: Download,
                finalException: Exception?,
            ) {
                if (download.request.uri.toString() == url) {
                    trySend(DownloadState.fromDownload(download))
                }
            }

            override fun onDownloadRemoved(
                downloadManager: DownloadManager,
                download: Download,
            ) {
                if (download.request.uri.toString() == url) {
                    trySend(DownloadState.NONE)
                }
            }
        }
        downloadManager.addListener(listener)

        awaitClose { downloadManager.removeListener(listener) }
    }

    /**
     * Returns the downloaded file as a media source.
     */
    fun getDownloadedMediaSource(url: String): MediaSource {
        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
        val cacheDataSourceFactory: CacheDataSource.Factory = CacheDataSource.Factory()
            .setCache(downloadCache)
            .setUpstreamDataSourceFactory(httpDataSourceFactory)

        val mediaSourceFactory: MediaSource.Factory =
            ProgressiveMediaSource.Factory(cacheDataSourceFactory)

        return mediaSourceFactory.createMediaSource(MediaItem.fromUri(url))
    }

    data class DownloadState(val state: Int, val progress: Float) {
        companion object {
            val NONE = DownloadState(Download.STATE_FAILED, 0f)

            fun fromDownload(download: Download): DownloadState {
                val progress = if (download.contentLength == 0L) {
                    0f
                } else {
                    download.bytesDownloaded.toFloat() / download.contentLength
                }
                return DownloadState(download.state, progress)
            }
        }
    }
}
