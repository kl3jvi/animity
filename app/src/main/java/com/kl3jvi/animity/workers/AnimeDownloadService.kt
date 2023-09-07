package com.kl3jvi.animity.workers

import android.app.Notification
import android.content.Intent
import android.net.Uri
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadRequest
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.scheduler.PlatformScheduler
import com.google.android.exoplayer2.scheduler.Scheduler
import com.google.android.exoplayer2.ui.DownloadNotificationHelper
import com.kl3jvi.animity.R
import com.kl3jvi.animity.data.downloader.getMimeType
import com.kl3jvi.animity.data.downloader.toMd5
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AnimeDownloadService : DownloadService(
    FOREGROUND_NOTIFICATION_ID,
    DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
    CHANNEL_ID,
    R.string.channel_name, // You'll have to provide this in strings.xml
    R.string.anime_provider, // Similarly, provide this in strings.xml
) {
    @Inject
    lateinit var downloadManagerDependency: DownloadManager

    companion object {
        private const val CHANNEL_ID = "ANIMITY_NOTIFICATIONS_CHANNEL_ID"
        const val FOREGROUND_NOTIFICATION_ID = 0x2
        const val JOB_SCHEDULER_SERVICE_JOB_ID = 10
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.getStringExtra("DOWNLOAD_URL")?.let { url ->
            val downloadId = "Anime_${url.toMd5()}"
            val downloadRequest: DownloadRequest =
                DownloadRequest.Builder(downloadId, Uri.parse(url))
                    .setMimeType(url.getMimeType())
                    .build()
            downloadManagerDependency.addDownload(downloadRequest)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun getDownloadManager(): DownloadManager = downloadManagerDependency

    override fun getScheduler(): Scheduler = PlatformScheduler(this, JOB_SCHEDULER_SERVICE_JOB_ID)

    override fun getForegroundNotification(
        downloads: MutableList<Download>,
        notMetRequirements: Int,
    ): Notification {
        return DownloadNotificationHelper(this, CHANNEL_ID).buildProgressNotification(
            this,
            R.drawable.ic_notification_icon,
            null,
            null,
            downloads,
            notMetRequirements,
        )
    }
}
