package com.kl3jvi.animity.analytics

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.kl3jvi.animity.BuildConfig
import javax.inject.Inject

/**
 * Analytics is a class that provides methods for logging events and setting user properties in
 * Firebase Analytics.
 *
 * @property firebaseAnalytics An instance of Firebase Analytics.
 */
class Analytics @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
) {

    /**
     * Removes special characters from the given string, except for underscores (_).
     *
     * @param string The string to remove special characters from.
     * @return The string with special characters removed.
     */
    private fun String.removeSpecialCharacters() = replace("[ ,:](?!_)".toRegex(), "")

    /**
     * Logs an event with the given name and parameters to Firebase Analytics.
     *
     * @param event The name of the event to log.
     * @param params A map of parameters to include with the event (optional).
     */
    fun logEvent(event: String, params: Map<String, Any?>? = null) {
        val bundle = params?.let { createBundle(it) }

        firebaseAnalytics.logReleaseEvent(event.removeSpecialCharacters(), bundle)
    }

    /**
     * Sets a user property with the given name and value in Firebase Analytics.
     *
     * @param name The name of the user property to set.
     * @param value The value to set the user property to (optional).
     */
    fun setUserProperty(name: String, value: String?) {
        firebaseAnalytics.setUserPropertyRelease(name, value)
    }

    /**
     * Logs the current screen to Firebase Analytics.
     *
     * @param context The current Context object.
     * @param screenName A string representing the name of the current screen.
     * @param screenClassOverride An optional string representing the name of the screen class to use in logs. If null, the class name of the Context will be used.
     */
    fun logCurrentScreen(screenName: String) {
        val screenDetails = mapOf(FirebaseAnalytics.Param.SCREEN_NAME to screenName)
        if (BuildConfig.BUILD_TYPE == "release") {
            logEvent(
                FirebaseAnalytics.Event.SCREEN_VIEW,
                screenDetails,
            )
        }
    }

    /**
     * Creates a Bundle from the given map of parameters.
     *
     * @param params A map of parameters to include in the Bundle.
     * @return A Bundle containing the given parameters.
     */
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

private fun FirebaseAnalytics.setUserPropertyRelease(name: String, value: String?) {
    if (BuildConfig.BUILD_TYPE == "release") setUserProperty(name, value)
}

private fun FirebaseAnalytics.logReleaseEvent(
    removeSpecialCharacters: String,
    bundle: Bundle?,
) = apply {
    if (BuildConfig.BUILD_TYPE == "release") logEvent(removeSpecialCharacters, bundle)
}
