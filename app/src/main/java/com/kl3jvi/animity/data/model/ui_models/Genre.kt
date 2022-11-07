package com.kl3jvi.animity.data.model.ui_models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Genre(
    val name: String = ""
) : Parcelable
