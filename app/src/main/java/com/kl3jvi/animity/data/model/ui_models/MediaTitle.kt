package com.kl3jvi.animity.data.model.ui_models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaTitle(
    val romaji: String = "",
    val english: String = "",
    val native: String = "",
    val userPreferred: String = "",
) : Parcelable
