package com.kl3jvi.animity.services

import android.app.Notification
import android.content.Context
import android.widget.Toast
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.scheduler.Scheduler
import com.google.android.exoplayer2.ui.DownloadNotificationHelper
import com.kl3jvi.animity.R
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject


class VideoDownloadService @Inject constructor(private val manager: DownloadManager) :
    DownloadService(
        1,
        DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
        "my app",
        R.string.app_name,
        R.string.title_home
    ) {
    private lateinit var notificationHelper: DownloadNotificationHelper
    private lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        context = this
        notificationHelper = DownloadNotificationHelper(this, "Animity")
    }


    override fun getDownloadManager(): DownloadManager {
        manager.maxParallelDownloads = 5
        manager.addListener(object : DownloadManager.Listener {
            override fun onDownloadRemoved(downloadManager: DownloadManager, download: Download) {
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
            }

            override fun onDownloadsPausedChanged(
                downloadManager: DownloadManager,
                downloadsPaused: Boolean
            ) {
                if (downloadsPaused) {
                    Toast.makeText(this@VideoDownloadService, "paused", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@VideoDownloadService, "resumed", Toast.LENGTH_SHORT).show()
                }

            }
        })
        return manager
    }

    override fun getScheduler(): Scheduler? {
        return null
    }

    override fun getForegroundNotification(downloads: MutableList<Download>): Notification {
        return notificationHelper.buildProgressNotification(
            this@VideoDownloadService,
            R.drawable.search_icon,
            null,
            "",
            downloads,
        )
    }
}