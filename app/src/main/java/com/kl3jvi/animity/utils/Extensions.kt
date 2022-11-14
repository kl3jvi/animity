package com.kl3jvi.animity.utils

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat


/* It's a function that takes a resource ID and returns a string. */
fun Fragment.getPreferenceKey(@StringRes resourceId: Int): String = getString(resourceId)

/**
 * Find a preference with the corresponding key and throw if it does not exist.
 * @param preferenceId Resource ID from preference_keys
 */
fun <T : Preference> PreferenceFragmentCompat.requirePreference(@StringRes preferenceId: Int) =
    requireNotNull(findPreference<T>(getPreferenceKey(preferenceId)))