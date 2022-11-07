package com.kl3jvi.animity.data.model.ui_models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserAvatar(
    val large: String = "",
    val medium: String = ""
) : Parcelable {
    fun getImageUrl(): String {
        return large
    }
}
