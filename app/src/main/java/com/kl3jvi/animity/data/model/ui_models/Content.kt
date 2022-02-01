package com.kl3jvi.animity.data.model.ui_models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Content(
    @PrimaryKey var episodeUrl: String = "",
    @ColumnInfo var animeName: String = "",
    @ColumnInfo var episodeNumber: String? = "",
    @ColumnInfo var watchedDuration: Long = 0,
    @ColumnInfo var duration: Long = 0,
) : Parcelable {
    fun getWatchedPercentage(): Int = ((watchedDuration * 100) / duration).toInt()
}
