package com.kl3jvi.animity.data.model.ui_models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kl3jvi.animity.ui.adapters.homeAdapter.HomeRecyclerViewItem
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "anime_table")
data class AnimeMetaModel(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo var typeValue: Int? = null,
    @ColumnInfo var imageUrl: String = "",
    @ColumnInfo var categoryUrl: String? = null,
    @ColumnInfo var episodeUrl: String? = null,
    @ColumnInfo var title: String = "",
    @ColumnInfo var episodeNumber: String? = null,
    @ColumnInfo var timestamp: Long = System.currentTimeMillis(),
    @ColumnInfo var insertionOrder: Int = -1,
    @ColumnInfo var genreList: List<GenreModel>? = null,
    @ColumnInfo var releasedDate: String? = null,
) : Parcelable

fun AnimeMetaModel.toAnime(): HomeRecyclerViewItem.Anime {
    return HomeRecyclerViewItem.Anime(
        id,
        typeValue,
        imageUrl,
        categoryUrl,
        episodeUrl,
        title,
        episodeNumber,
        timestamp,
        insertionOrder
    )
}





