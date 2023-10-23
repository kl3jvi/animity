@file:Suppress("DEPRECATION")

package com.kl3jvi.animity.workers

import android.app.Notification
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.scheduler.PlatformScheduler
import com.google.android.exoplayer2.scheduler.Scheduler
import com.google.android.exoplayer2.ui.DownloadNotificationHelper
import com.kl3jvi.animity.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class AnimeDownloadService : DownloadService(
    FOREGROUND_NOTIFICATION_ID,
    DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
    CHANNEL_ID,
    R.string.channel_name,
    R.string.anime_provider,
) {
    @Inject
    lateinit var downloadManagerDependency: DownloadManager
    private val notificationHelper: DownloadNotificationHelper by lazy {
        DownloadNotificationHelper(this, CHANNEL_ID)
    }

    companion object {
        private const val CHANNEL_ID = "ANIMITY_NOTIFICATIONS_CHANNEL_ID"
        const val FOREGROUND_NOTIFICATION_ID = 0x2
        const val JOB_SCHEDULER_SERVICE_JOB_ID = 10
    }

    override fun getDownloadManager(): DownloadManager = downloadManagerDependency

    override fun getScheduler(): Scheduler = PlatformScheduler(this, JOB_SCHEDULER_SERVICE_JOB_ID)

    override fun getForegroundNotification(
        downloads: MutableList<Download>,
        notMetRequirements: Int,
    ): Notification {
        return buildNotification(downloads, notMetRequirements)
    }

    private fun buildNotification(
        downloads: List<Download>,
        notMetRequirements: Int,
    ): Notification {
        return notificationHelper.buildProgressNotification(
            this,
            R.drawable.ic_notification_icon,
            null,
            null,
            downloads,
            notMetRequirements,
        )
    }
}
