package com.kl3jvi.animity.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EpisodeInfo(
    var vidCdnUrl: String?=null,
    var nextEpisodeUrl: String? = null,
    var previousEpisodeUrl: String? = null
) : Parcelable