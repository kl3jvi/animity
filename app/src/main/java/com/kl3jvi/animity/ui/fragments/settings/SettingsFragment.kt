package com.kl3jvi.animity.ui.fragments.settings

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.viewModels
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
import com.kl3jvi.animity.utils.collect
import com.kl3jvi.animity.utils.configurePreference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var settings: Settings

    @Inject
    lateinit var analytics: Analytics

    private val viewModel by viewModels<SettingsViewModel>()

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
        collect(viewModel.versionInfo) {
            Log.e("URLJAAA", it.Animity?.universal?.direct_link.orEmpty())
            it.Animity?.let {
                val manager = DownloadManager.Builder(requireActivity()).run {
                    apkUrl(it.universal.direct_link)
                    apkName("animity.apk")
                    smallIcon(R.mipmap.ic_launcher)
                    apkVersionCode(it.universal.versionCode)
                    apkVersionName(it.universal.versionName)
                    apkSize(it.universal.apkSize)
                    apkDescription(it.universal.update_message)
                    apkVersionCode(BuildConfig.VERSION_CODE + 1)
                }
                if (it.universal.versionCode > BuildConfig.VERSION_CODE) {
                    manager.build()
                        .download()
                } else showNoUpdate()
            }
        }
        return true
    }

    private fun showNoUpdate() {
        AlertDialog.Builder(requireContext())
            .setTitle("Update Check")
            .setMessage("No updates available at the moment.")
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .show()

    }

    companion object {
        const val PAY_PAY_URL = "https://paypal.me/kl3jvi"
    }
}