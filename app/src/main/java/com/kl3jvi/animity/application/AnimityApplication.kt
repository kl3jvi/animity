package com.kl3jvi.animity.application

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.kl3jvi.animity.utils.Constants.Companion.ONESIGNAL_APP_ID

import com.onesignal.OneSignal
import dagger.hilt.android.HiltAndroidApp

/* A class that extends `Context` and is the base class for those who need to maintain global
application state. */
@HiltAndroidApp
class AnimityApplication : Application() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate() {
        super.onCreate()
        firebaseAnalytics = Firebase.analytics
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
    }

}
