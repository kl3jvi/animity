package com.kl3jvi.animity.data.model.ui_models

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Entity
@Parcelize
data class EpisodeModel(
    var episodeName: String = "",
    var episodeNumber: String,
    var episodeUrl: String,
    var episodeType: String,
    var percentage: Int = 0,
    var isFiller: Boolean = false
) : Parcelable


data class EpisodeWithTitle(
    @SerializedName("episodes")
    val episodes: List<Episode>? = emptyList(),
)

data class Episode(
    @SerializedName("number")
    val number: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("filler-bool")
    val isFiller: Boolean,
)