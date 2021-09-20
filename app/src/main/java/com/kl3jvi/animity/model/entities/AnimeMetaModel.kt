package com.kl3jvi.animity.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AnimeMetaModel(
    var ID: Int? = null,
    var typeValue: Int? = null,
    var imageUrl: String = "",
    var categoryUrl: String? = null,
    var episodeUrl: String? = null,
    var title: String = "",
    var episodeNumber: String? = null,
    var timestamp: Long = System.currentTimeMillis(),
    var insertionOrder: Int = -1,
    var genreList: List<GenreModel>? = null,
    var releasedDate: String? = null

) : Parcelable