package com.kl3jvi.animity.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GenreModel(
    var genreUrl: String = "",
    var genreName: String = ""
) : Parcelable