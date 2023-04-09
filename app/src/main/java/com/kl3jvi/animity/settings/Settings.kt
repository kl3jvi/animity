package com.kl3jvi.animity.settings

import android.content.Context
import android.content.SharedPreferences
import com.kl3jvi.animity.R
import com.kl3jvi.animity.ui.activities.player.DnsTypes
import com.kl3jvi.animity.utils.getPreferenceKey
import javax.inject.Inject

class Settings @Inject constructor(
    appContext: Context,
    override val preferences: SharedPreferences
) : PreferencesHolder {

    var skipIntroDelay by longPreference(
        appContext.getPreferenceKey(R.string.settings),
        default = 0L
    )

    var selectedProvider by enumPreference(
        appContext.getPreferenceKey(R.string.anime_provider),
        AnimeTypes.GOGO_ANIME
    )

    var selectedDns by enumPreference(
        appContext.getPreferenceKey(R.string.dns_provider),
        DnsTypes.GOOGLE_DNS
    )
}

enum class AnimeTypes {
    GOGO_ANIME,
    ENIME
}
