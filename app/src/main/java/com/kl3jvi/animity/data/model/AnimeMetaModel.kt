package com.kl3jvi.animity.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
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
    @ColumnInfo(name = "favorite_anime") var favoriteAnime: Boolean = false,
    @ColumnInfo var synopsis: String? = null
) : Parcelable
