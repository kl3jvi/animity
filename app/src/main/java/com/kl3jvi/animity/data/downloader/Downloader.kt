package com.kl3jvi.animity.data.downloader

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.FileDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.kl3jvi.animity.workers.AnimeDownloadService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.security.MessageDigest
import javax.inject.Inject

class Downloader @Inject constructor(
    private val downloadManager: DownloadManager,
    @ApplicationContext private val context: Context,
    private val simpleCache: SimpleCache,
) {

    val currentState = callbackFlow<DownloadState> {
        val listener = object : DownloadManager.Listener {
            override fun onDownloadChanged(
                downloadManager: DownloadManager,
                download: Download,
                finalException: Exception?,
            ) {
                super.onDownloadChanged(downloadManager, download, finalException)
                Log.e("Download STATE", download.state.toString())
                trySend(DownloadState.getNameFromCount(download.state))
            }
        }
        downloadManager.addListener(listener)
        awaitClose { downloadManager.removeListener(listener) }
    }

    fun downloadM3U8Content(url: String) {
        val intent = Intent(context, AnimeDownloadService::class.java).apply {
            putExtra("DOWNLOAD_URL", url)
        }
        ContextCompat.startForegroundService(context, intent)
    }

    fun getCachedMediaItem(url: String): HlsMediaSource {
        val cacheReadDataSourceFactory = CacheDataSource.Factory()
            .setCache(simpleCache)
            .setUpstreamDataSourceFactory(FileDataSource.Factory())
            .setCacheWriteDataSinkFactory(null)

        val cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(simpleCache)
            .setUpstreamDataSourceFactory(cacheReadDataSourceFactory)

        return HlsMediaSource.Factory(cacheDataSourceFactory)
            .createMediaSource(MediaItem.fromUri(url))
    }

    fun getAllDownloadedMediaItems(): List<MediaItem> {
        return downloadManager.currentDownloads.map {
            MediaItem.fromUri(it.request.uri)
        }
    }
}

fun String.getMimeType(): String? {
    return when {
        this.endsWith(".m3u8", ignoreCase = true) -> "application/x-mpegURL" // HLS
        this.endsWith(".mpd", ignoreCase = true) -> "application/dash+xml" // DASH
        else -> null
    }
}

fun String.toMd5(): String {
    val bytes = MessageDigest.getInstance("MD5").digest(this.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}

enum class DownloadState(val count: Int) {
    QUEUED(0),
    DOWNLOADING(1),
    COMPLETED(2),
    FAILED(3),
    REMOVED(4),
    UNKNOWN(5),
    ;

    companion object {
        fun getNameFromCount(count: Int): DownloadState {
            return valueOf(
                when (count) {
                    0 -> "QUEUED"
                    1 -> "DOWNLOADING"
                    2 -> "COMPLETED"
                    3 -> "FAILED"
                    4 -> "REMOVED"
                    5 -> "UNKNOWN"
                    else -> "UNKNOWN"
                },
            )
        }
    }
}
