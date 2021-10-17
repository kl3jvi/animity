package com.kl3jvi.animity.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AnimeInfoModel(
    var id: String,
    var animeTitle: String,
    var imageUrl: String,
    var type: String,
    var releasedTime: String,
    var status: String,
    var genre: ArrayList<GenreModel>,
    var plotSummary: String,
    var alias: String,
    var endEpisode: String
) : Parcelable


