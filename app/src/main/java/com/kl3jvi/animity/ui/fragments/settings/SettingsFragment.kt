package com.kl3jvi.animity.ui.fragments.settings

import android.os.Bundle
import android.util.Log
import androidx.preference.DropDownPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.kl3jvi.animity.R
import com.kl3jvi.animity.settings.Settings
import com.kl3jvi.animity.settings.toStringGson
import com.kl3jvi.animity.utils.edit
import com.kl3jvi.animity.utils.getPreferenceKey
import com.kl3jvi.animity.utils.requirePreference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
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
        requirePreference<DropDownPreference>(R.string.anime_provider).apply {
            Log.e("VALUE", value?.toString() ?: "_NUNU")
            onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
                settings.preferences.edit {
                    putString(
                        getPreferenceKey(R.string.anime_provider),
                        newValue.toStringGson()
                    )
                }
                true
            }
        }
    }
}
