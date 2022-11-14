package com.kl3jvi.animity.settings

import android.content.SharedPreferences
import com.kl3jvi.animity.utils.Constants
import javax.inject.Inject

class Settings @Inject constructor(
    override val preferences: SharedPreferences
) : PreferencesHolder {

    var skipIntroDelay by longPreference(
        Constants.SHARED_PREFERENCES_NAME,
        default = 0L
    )

}

