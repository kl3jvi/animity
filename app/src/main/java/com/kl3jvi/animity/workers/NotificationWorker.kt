package com.kl3jvi.animity.workers

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.hilt.work.HiltWorker
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kl3jvi.animity.R
import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.model.ui_models.Notification
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient
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
            val notifications =
                aniListGraphQlClient.getNotifications().data?.convert()?.firstOrNull()
                    ?: Notification()

            // Check if the notification is new
            if (!isNotificationIdStored(notifications.id)) {
                Log.e("Notifications received", notifications.toString())

                showNotification(notifications)

                // Store the notification's id to avoid showing it again
                storeNotificationId(notifications.id)
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun showNotification(notification: Notification) {
        val args = bundleOf("animeDetails" to notification.media)

        val pendingIntent = NavDeepLinkBuilder(applicationContext)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.mobile_navigation)
            .setDestination(R.id.navigation_details)
            .setArguments(args)
            .createPendingIntent()

        val builder =
            NotificationCompat.Builder(applicationContext, ANIMITY_NOTIFICATIONS_CHANNEL_ID)
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

    private fun isNotificationIdStored(id: Int?): Boolean {
        return preferences.getBoolean(id.toString(), false)
    }

    private fun storeNotificationId(id: Int?) {
        preferences.edit().putBoolean(id.toString(), true).apply()
    }

    private companion object {
        const val ANIMITY_NOTIFICATIONS_CHANNEL_ID = "ANIMITY_NOTIFICATIONS_CHANNEL_ID"
        const val NOTIFICATION_ID = 1
    }
}
