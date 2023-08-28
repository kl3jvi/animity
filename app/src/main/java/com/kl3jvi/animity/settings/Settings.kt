package com.kl3jvi.animity.settings

import android.content.Context
import android.content.SharedPreferences
import com.kl3jvi.animity.R
import com.kl3jvi.animity.data.enums.AnimeTypes
import com.kl3jvi.animity.data.enums.DnsTypes
import com.kl3jvi.animity.utils.getPreferenceKey
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Settings @Inject constructor(
    appContext: Context,
    override val preferences: SharedPreferences,
) : PreferencesHolder {

    var skipIntroDelay by longPreference(
        appContext.getPreferenceKey(R.string.skip_delay),
        default = 85_000L,
    )

    var seekForwardTime by longPreference(
        appContext.getPreferenceKey(R.string.forward_seek),
        default = 10_000L,
    )

    var seekBackwardTime by longPreference(
        appContext.getPreferenceKey(R.string.backward_seek),
        default = 10_000L,
    )

    var pipEnabled by booleanPreference(
        appContext.getPreferenceKey(R.string.pip),
        default = false,
    )

    var selectedProvider by enumPreference(
        appContext.getPreferenceKey(R.string.anime_provider),
        AnimeTypes.GOGO_ANIME,
    )

    var selectedDns by enumPreference(
        appContext.getPreferenceKey(R.string.dns_provider),
        DnsTypes.GOOGLE_DNS,
    )

    var openCount by intPreference(
        appContext.getPreferenceKey(R.string.open_counter),
        0,
    )

    var maybeLater by booleanPreference(
        appContext.getPreferenceKey(R.string.maybe_later),
        false,
    )
}
