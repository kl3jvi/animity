package com.kl3jvi.animity.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class Analytics @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) {

    private val pattern = "[ ,:](?!_)".toRegex()

    fun logEvent(event: String, params: Map<String, Any?>? = null) {
        val bundle = params?.let { createBundle(it) }
        firebaseAnalytics.logEvent(event, bundle)
    }

    fun setUserProperty(name: String, value: String?) {
        firebaseAnalytics.setUserProperty(name, value)
    }

    private fun createBundle(params: Map<String, Any?>): Bundle {
        val bundle = Bundle()
        params.forEach { (key, value) ->
            when (value) {
                is String -> bundle.putString(key, value)
                is Boolean -> bundle.putBoolean(key, value)
                is Int -> bundle.putInt(key, value)
                is Long -> bundle.putLong(key, value)
                is Double -> bundle.putDouble(key, value)
                is Float -> bundle.putFloat(key, value)
            }
        }
        return bundle
    }
}
