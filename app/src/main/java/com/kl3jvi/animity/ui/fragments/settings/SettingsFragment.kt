package com.kl3jvi.animity.ui.fragments.settings

import android.net.Uri
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.preference.DropDownPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import com.azhon.appupdate.manager.DownloadManager
import com.kl3jvi.animity.BuildConfig
import com.kl3jvi.animity.R
import com.kl3jvi.animity.analytics.Analytics
import com.kl3jvi.animity.data.enums.DnsTypes
import com.kl3jvi.animity.settings.Settings
import com.kl3jvi.animity.settings.toJson
import com.kl3jvi.animity.ui.widgets.ColorSwitchPreferenceCompat
import com.kl3jvi.animity.ui.widgets.CustomPreference
import com.kl3jvi.animity.utils.configurePreference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var settings: Settings

    @Inject
    lateinit var analytics: Analytics

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onResume() {
        super.onResume()
        setupPreferences()
    }

    private fun setupPreferences() {
        configurePreference<DropDownPreference>(
            R.string.anime_provider,
            settings.preferences
        ) { summary = value.toJson()?.removePrefix("\"")?.removeSuffix("\"") }

        configurePreference<DropDownPreference>(
            R.string.dns_provider,
            settings.preferences
        ) { entries = DnsTypes.dnsEntries }

        configurePreference<SeekBarPreference>(
            R.string.skip_delay,
            settings.preferences
        ) { seekBarIncrement = 1000 }

        configurePreference<SeekBarPreference>(
            R.string.forward_seek,
            settings.preferences
        ) { seekBarIncrement = 1000 }

        configurePreference<SeekBarPreference>(
            R.string.backward_seek,
            settings.preferences
        ) { seekBarIncrement = 1000 }

        configurePreference<ColorSwitchPreferenceCompat>(
            R.string.pip,
            settings.preferences,
            clickListener = {
                analytics.setUserProperty("PIP", "clicked")
                true
            }
        )

        configurePreference<CustomPreference>(
            R.string.donation,
            settings.preferences,
            clickListener = {
                CustomTabsIntent.Builder()
                    .build()
                    .launchUrl(requireContext(), Uri.parse(PAY_PAY_URL))
                analytics.setUserProperty("Donation", "clicked")
                true
            }
        )

        configurePreference<CustomPreference>(
            R.string.check_for_update_key,
            settings.preferences,
            ::checkUpdates
        )
    }

    private fun checkUpdates(preference: Preference): Boolean {
        val manager = DownloadManager.Builder(requireActivity()).run {
            apkUrl("https://github.com/kl3jvi/animity/releases/download/v0.1.7/Animity-v1.1.2-universal-release.apk")
            apkName("animity.apk")
            smallIcon(R.mipmap.ic_launcher)
            apkVersionCode(2)
            apkVersionName("v0.0.18")
            apkSize("6.42MB")
            apkDescription("Improved searching and trying to add personalised content.")
            apkVersionCode(BuildConfig.VERSION_CODE + 1)
            build()
        }
        manager.download()
        return true
    }

    companion object {
        const val PAY_PAY_URL = "https://paypal.me/kl3jvi"
    }
}