package com.kl3jvi.animity.ui.fragments.settings

import android.os.Bundle
import androidx.preference.DropDownPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import androidx.preference.SwitchPreference
import com.kl3jvi.animity.R
import com.kl3jvi.animity.data.enums.DnsTypes
import com.kl3jvi.animity.settings.Settings
import com.kl3jvi.animity.settings.toJson
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
            onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
                settings.preferences.edit {
                    putString(
                        getPreferenceKey(R.string.anime_provider),
                        newValue.toJson()
                    )
                }
                summary = newValue.toJson()
                true
            }
        }

        requirePreference<DropDownPreference>(R.string.dns_provider).apply {
            entries = DnsTypes.dnsEntries
            onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
                settings.preferences.edit {
                    putString(
                        getPreferenceKey(R.string.dns_provider),
                        newValue.toJson()
                    )
                }
                true
            }
        }

        requirePreference<SeekBarPreference>(R.string.skip_delay).apply {
            seekBarIncrement = 1000
            onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
                settings.preferences.edit {
                    putLong(
                        getPreferenceKey(R.string.skip_delay),
                        (newValue as Int).times(1000).toLong()
                    )
                }
                true
            }
        }

        requirePreference<SeekBarPreference>(R.string.forward_seek).apply {
            seekBarIncrement = 1000
            onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
                settings.preferences.edit {
                    putLong(
                        getPreferenceKey(R.string.forward_seek),
                        (newValue as Int).times(1000).toLong()
                    )
                }
                true
            }
        }

        requirePreference<SeekBarPreference>(R.string.backward_seek).apply {
            seekBarIncrement = 1000
            onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
                settings.preferences.edit {
                    putLong(
                        getPreferenceKey(R.string.backward_seek),
                        (newValue as Int).times(1000).toLong()
                    )
                }
                true
            }
        }

        requirePreference<SwitchPreference>(R.string.pip).apply {
            onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
                settings.preferences.edit {
                    putBoolean(
                        getPreferenceKey(R.string.pip),
                        (newValue as? Boolean) ?: false
                    )
                }
                true
            }
        }
    }
}
