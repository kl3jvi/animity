package com.kl3jvi.animity.application

import android.app.Application
import android.os.StrictMode
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AnimityApplication : Application() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate() {
        turnOnStrictMode()
        super.onCreate()
        firebaseAnalytics = Firebase.analytics
        apply {
//            OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
//            OneSignal.initWithContext(this)
//            OneSignal.setAppId(ONESIGNAL_APP_ID)
        }
    }

    private fun turnOnStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder().detectAll()
                .penaltyLog()

                .penaltyFlashScreen().build()
        )
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder().detectAll()
                .penaltyLog().build()
        )
    }


}
