package com.kl3jvi.animity.services

import android.app.Notification
import android.widget.Toast
import androidx.navigation.NavDeepLinkBuilder
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.scheduler.Scheduler
import com.google.android.exoplayer2.ui.DownloadNotificationHelper
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.kl3jvi.animity.R
import com.kl3jvi.animity.utils.Constants.Companion.DOWNLOAD_CHANNEL_DESCRIPT
import com.kl3jvi.animity.utils.Constants.Companion.DOWNLOAD_CHANNEL_ID
import com.kl3jvi.animity.utils.Constants.Companion.getDataSourceFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class VideoDownloadService :
    DownloadService(
        getRandomId(),
        DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
        DOWNLOAD_CHANNEL_ID,
        R.string.app_name,
        R.string.title_home
    ) {
    private lateinit var notificationHelper: DownloadNotificationHelper

    private lateinit var manager: DownloadManager

    @Inject
    lateinit var downloadCache: SimpleCache

    @Inject
    lateinit var exoDatabaseProvider: StandaloneDatabaseProvider

    override fun onCreate() {
        super.onCreate()

        notificationHelper = DownloadNotificationHelper(this, DOWNLOAD_CHANNEL_ID)
    }

    override fun getDownloadManager(): DownloadManager {
//        val appContainer = (applicationContext as AnimityApplication).appContainer
        manager = DownloadManager(
            this,
            exoDatabaseProvider,
            downloadCache,
            getDataSourceFactory(),
            Runnable::run
        )
        manager.maxParallelDownloads = 3
        manager.addListener(object : DownloadManager.Listener {
            override fun onDownloadRemoved(downloadManager: DownloadManager, download: Download) {
                Toast.makeText(this@VideoDownloadService, "Deleted", Toast.LENGTH_SHORT).show()
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

    override fun getForegroundNotification(
        downloads: MutableList<Download>,
        notMetRequirements: Int
    ): Notification {
        val contentIntent = NavDeepLinkBuilder(this)
            .setGraph(R.navigation.mobile_navigation)
            .setDestination(R.id.navigation_downloads)
            .setArguments(null)
            .createPendingIntent()

        return notificationHelper.buildProgressNotification(
            this@VideoDownloadService,
            R.drawable.ic_downloading,
            contentIntent,
            DOWNLOAD_CHANNEL_DESCRIPT,
            downloads,
            notMetRequirements
        )
    }
}

fun getRandomId(): Int {
    return System.currentTimeMillis().toInt()
}
