@file:Suppress("DEPRECATION")

package com.kl3jvi.animity.data.downloader

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadRequest
import com.google.android.exoplayer2.offline.DownloadService
import com.kl3jvi.animity.workers.AnimeDownloadService
import java.security.MessageDigest
import javax.inject.Inject

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
    STATE_QUEUED(0),
    STATE_STOPPED(1),
    STATE_DOWNLOADING(2),
    STATE_COMPLETED(3),
    STATE_FAILED(4),
    STATE_REMOVING(5),
    STATE_RESTARTING(7),
    ;

    companion object {
        fun getNameFromCount(count: Int): DownloadState {
            return valueOf(
                when (count) {
                    0 -> "STATE_QUEUED"
                    1 -> "STATE_STOPPED"
                    2 -> "STATE_DOWNLOADING"
                    3 -> "STATE_COMPLETED"
                    4 -> "STATE_FAILED"
                    5 -> "STATE_REMOVING"
                    7 -> "STATE_RESTARTING"
                    else -> "STATE_QUEUED"
                },
            )
        }
    }
}

class Downloader
@Inject
constructor(
    private val appContext: Context,
    private val downloadManager: DownloadManager,
) {
    init {
        getDownloadedVideos().forEach {
            Log.e("Downloader", "Downloaded video: ${it.mediaId}")
        }
    }

    fun downloadVideoUrl(url: String, downloadManagerListener: DownloadManager.Listener) {
        DownloadService.sendAddDownload(
            appContext,
            AnimeDownloadService::class.java,
            DownloadRequest.Builder(
                "Anime_${url.toMd5()}",
                Uri.parse(url),
            ).setMimeType(url.getMimeType())
                .build(),
            false,
        )
        downloadManager.addListener(downloadManagerListener)
    }

    fun getDownloadedVideos(): List<MediaItem> {
        val downloads = downloadManager.currentDownloads
        return downloads.mapNotNull { download ->
            if (download.state == Download.STATE_COMPLETED) {
                MediaItem.Builder()
                    .setUri(download.request.uri)
                    .setMediaId(download.request.id)
                    .build()
            } else {
                null
            }
        }
    }
}
