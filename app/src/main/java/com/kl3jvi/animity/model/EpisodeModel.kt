package com.kl3jvi.animity.model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class EpisodeModel(
    var episodeNumber: String,
    var episodeUrl: String,
    var episodeType: String,
    var percentage: Int = 0,
) : Parcelable
