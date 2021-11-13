package com.kl3jvi.animity.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Content(
    var url: String? = "",
    var animeName: String = "",
    var episodeName: String? = "",
    var episodeUrl: String? = "",
    var nextEpisodeUrl: String? = null,
    var previousEpisodeUrl: String? = null,
    var watchedDuration: Long = 0,
    var duration: Long = 0,
    var insertionTime: Long = 0
) : Parcelable
