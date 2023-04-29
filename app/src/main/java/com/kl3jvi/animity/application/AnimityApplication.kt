package com.kl3jvi.animity.application

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.kl3jvi.animity.R
import com.kl3jvi.animity.workers.NotificationWorker
import com.onesignal.OneSignal
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/* A class that extends `Context` and is the base class for those who need to maintain global
application state. */
@HiltAndroidApp
class AnimityApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
        createNotificationChannel()
        setupNotificationWorker()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun setupNotificationWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(false)
            .build()

        // Check if there is an existing periodic work request with the specified tag
        val workInfos = WorkManager.getInstance(this).getWorkInfosByTag(TAG_PERIODIC_WORK_REQUEST)
        val hasExistingWorkRequest = workInfos.get().isNotEmpty()

        // Schedule a new periodic work request only if there is no existing one
        if (!hasExistingWorkRequest) {
            val work: PeriodicWorkRequest =
                PeriodicWorkRequestBuilder<NotificationWorker>(15, TimeUnit.MINUTES)
                    .setConstraints(constraints)
                    .addTag(TAG_PERIODIC_WORK_REQUEST)
                    .build()

            WorkManager.getInstance(this).enqueue(work)
        }
    }


    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(ANIMITY_NOTIFICATIONS_CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .setMinimumLoggingLevel(Log.DEBUG)
        .build()


    companion object {
        const val ANIMITY_NOTIFICATIONS_CHANNEL_ID = "ANIMITY_NOTIFICATIONS_CHANNEL_ID"
        const val ONESIGNAL_APP_ID = "f8d936f4-2d9f-4c53-9f85-e2d3789d9174"
        const val TAG_PERIODIC_WORK_REQUEST = "periodic_work_request"
    }
}
