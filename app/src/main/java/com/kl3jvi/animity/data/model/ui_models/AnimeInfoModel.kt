package com.kl3jvi.animity.data.model.ui_models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AnimeInfoModel(
    var id: String,
    var alias: String,
    var endEpisode: String
) : Parcelable
