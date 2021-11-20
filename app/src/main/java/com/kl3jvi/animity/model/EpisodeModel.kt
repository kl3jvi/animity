package com.kl3jvi.animity.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EpisodeModel(
    var episodeNumber: String,
    var episodeurl: String,
    var episodeType: String
) : Parcelable
