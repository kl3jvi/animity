package com.kl3jvi.animity.utils

import android.text.format.DateUtils
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import java.text.ParseException

/* It's a function that takes a resource ID and returns a string. */
fun Fragment.getPreferenceKey(@StringRes resourceId: Int): String = getString(resourceId)

/**
 * Find a preference with the corresponding key and throw if it does not exist.
 * @param preferenceId Resource ID from preference_keys
 */

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
