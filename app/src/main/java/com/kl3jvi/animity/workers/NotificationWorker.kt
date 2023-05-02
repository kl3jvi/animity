package com.kl3jvi.animity.workers

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.os.bundleOf
import androidx.hilt.work.HiltWorker
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kl3jvi.animity.R
import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.model.ui_models.Notification
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient
import com.kl3jvi.animity.data.paging.PagingDataItem
import com.kl3jvi.animity.ui.activities.main.MainActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val ioDispatcher: CoroutineDispatcher,
    private val aniListGraphQlClient: AniListGraphQlClient,
    private val preferences: SharedPreferences
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        try {
            val response = aniListGraphQlClient.getNotifications(1).data?.convert()

            val latestNotification = response?.flatMap {
                when (it) {
                    is PagingDataItem.NotificationItem -> listOf(it.notification)
                    else -> emptyList()
                }
            }?.maxByOrNull { it.id ?: -1 }
            if (latestNotification != null && !isNotificationIdStored(latestNotification.id)) {
                Log.e(TAG, "Notifications received: $latestNotification")
                showNotification(latestNotification)
                storeNotificationId(latestNotification.id)
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun showNotification(notification: Notification) {
        val args = bundleOf(
            "animeDetails" to notification.media,
            if (notification.episode != null) {
                "desiredPosition" to notification.episode
            } else {
                "desiredPosition" to -1
            }
        )

        val pendingIntent = NavDeepLinkBuilder(applicationContext)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.mobile_navigation)
            .setDestination(R.id.navigation_details)
            .setArguments(args)
            .createPendingIntent()

        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_circle_notifications_24)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
            .setContentText(notification.getFormattedNotification())
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        NotificationManagerCompat.from(applicationContext).apply {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notify(NOTIFICATION_ID, builder.build())
            }
        }
    }

    private fun isNotificationIdStored(id: Int?): Boolean =
        preferences.getBoolean(id.toString(), false)

    private fun storeNotificationId(id: Int?) = preferences.edit { putBoolean(id.toString(), true) }

    companion object {
        private const val TAG = "NotificationWorker"
        private const val CHANNEL_ID = "ANIMITY_NOTIFICATIONS_CHANNEL_ID"
        private const val NOTIFICATION_ID = 0x1
    }
}
