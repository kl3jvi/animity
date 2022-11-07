package com.kl3jvi.animity.data.model.ui_models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FuzzyDate(
    val year: Int? = null,
    val month: Int? = null,
    val day: Int? = null
) : Parcelable {
    private fun isNull(): Boolean {
        return year == null || month == null || day == null
    }

    fun getDate(): String {
        return if (!isNull()) {
            "$year/$month/$day"
        } else {
            "Unknown"
        }
    }
}
