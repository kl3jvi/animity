package com.kl3jvi.animity.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EpisodeInfo(
    var vidCdnUrl: String? = null,
) : Parcelable
