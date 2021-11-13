package com.kl3jvi.animity.application

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.kl3jvi.animity.utils.Constants.Companion.DOWNLOAD_CHANNEL_DESCRIPT
import com.kl3jvi.animity.utils.Constants.Companion.DOWNLOAD_CHANNEL_ID
import com.kl3jvi.animity.utils.Constants.Companion.DOWNLOAD_CHANNEL_NAME
import com.kl3jvi.animity.utils.Constants.Companion.ONESIGNAL_APP_ID
import com.onesignal.OneSignal
import dagger.hilt.android.HiltAndroidApp



@HiltAndroidApp
class AnimityApplication : Application() {

    lateinit var appContainer: AppContainer

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate() {
        super.onCreate()
        firebaseAnalytics = Firebase.analytics
        appContainer = AppContainer(this)

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        createNotificationChannel()
    }


    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = DOWNLOAD_CHANNEL_NAME
            val descriptionText = DOWNLOAD_CHANNEL_DESCRIPT
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(DOWNLOAD_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}