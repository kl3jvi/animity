package com.kl3jvi.animity.utils

import android.content.SharedPreferences
import android.util.Log
import androidx.annotation.StringRes
import androidx.core.content.edit
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.kl3jvi.animity.settings.toJson

fun <T : Preference> PreferenceFragmentCompat.configurePreference(
    @StringRes preferenceId: Int,
    preferences: SharedPreferences,
    block: T.() -> Unit = {}
): T {
    val preference = requirePreference<T>(preferenceId)
    preference.block()
    preference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
        val key = getPreferenceKey(preferenceId)
        preferences.edit {
            Log.e("NEW Value", newValue.toString())
            when (newValue) {
                is Boolean -> putBoolean(key, newValue)
                is Int -> putLong(key, newValue.toLong() * 1000)
                is String -> putString(key, newValue.toJson())
            }
        }
        true
    }
    return preference
}

private fun <T : Preference> PreferenceFragmentCompat.requirePreference(
    @StringRes preferenceId: Int
) = requireNotNull(findPreference<T>(getPreferenceKey(preferenceId)))
