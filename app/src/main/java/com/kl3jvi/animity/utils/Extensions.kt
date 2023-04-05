package com.kl3jvi.animity.utils

import android.content.SharedPreferences
import android.text.format.DateUtils
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import java.text.ParseException

/* It's a function that takes a resource ID and returns a string. */
fun Fragment.getPreferenceKey(@StringRes resourceId: Int): String = getString(resourceId)

/**
 * Find a preference with the corresponding key and throw if it does not exist.
 * @param preferenceId Resource ID from preference_keys
 */
fun <T : Preference> PreferenceFragmentCompat.requirePreference(@StringRes preferenceId: Int) =
    requireNotNull(findPreference<T>(getPreferenceKey(preferenceId)))

fun Int.parseTime(errorHappened: () -> Unit): CharSequence? {
    return try {
        val now = System.currentTimeMillis()
        DateUtils.getRelativeTimeSpanString(now, toLong(), DateUtils.MINUTE_IN_MILLIS)
    } catch (e: ParseException) {
        e.printStackTrace()
        errorHappened()
        ""
    }
}

inline fun SharedPreferences.edit(
    commit: Boolean = false,
    action: SharedPreferences.Editor.() -> Unit
) {
    val editor = edit()
    action(editor)
    if (commit) {
        editor.commit()
    } else {
        editor.apply()
    }
}
