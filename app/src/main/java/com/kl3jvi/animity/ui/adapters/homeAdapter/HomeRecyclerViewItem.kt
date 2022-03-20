package com.kl3jvi.animity.ui.adapters.homeAdapter

import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.data.model.ui_models.GenreModel

sealed class HomeRecyclerViewItem {


    data class Title(
        val id: Int,
        val title: String
    ) : HomeRecyclerViewItem()

    data class Anime(
        var id: Int = 0,
        var typeValue: Int? = null,
        var imageUrl: String = "",
        var categoryUrl: String? = null,
        var episodeUrl: String? = null,
        var title: String = "",
        var episodeNumber: String? = null,
        var timestamp: Long = System.currentTimeMillis(),
        var insertionOrder: Int = -1,
        var genreList: List<GenreModel>? = null,
        var releasedDate: String? = null,
    ) : HomeRecyclerViewItem()

    data class HorizontalAnimeWrapper(
        val animeList: List<AnimeMetaModel>,
        val id: Int = animeList.hashCode()
    ) : HomeRecyclerViewItem()


}

