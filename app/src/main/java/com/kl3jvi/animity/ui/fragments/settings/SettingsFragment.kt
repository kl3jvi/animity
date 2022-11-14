package com.kl3jvi.animity.ui.fragments.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import com.kl3jvi.animity.R
import com.kl3jvi.animity.settings.Settings
import com.kl3jvi.animity.utils.requirePreference
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var settings: Settings

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onResume() {
        super.onResume()
        setupPreferences()
    }

    private fun setupPreferences() {
        requirePreference<SeekBarPreference>(R.string.skip_intro_preference_key).apply {

//            onPreferenceChangeListener =
//                Preference.OnPreferenceChangeListener { preference, newValue ->
//
//                }
        }
    }
}