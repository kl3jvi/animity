package com.kl3jvi.animity.ui.fragments.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.kl3jvi.animity.R

class SettingsFragment : PreferenceFragmentCompat() {
    /**
     * "Set the preferences from the XML file with the given root key."
     *
     * The root key is the key that is used to identify the root preference
     *
     * @param savedInstanceState The saved instance state from the activity that created this fragment.
     * @param rootKey The preference hierarchy you are building.
     */
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}