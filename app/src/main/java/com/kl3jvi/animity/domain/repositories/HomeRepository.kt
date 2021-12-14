package com.kl3jvi.animity.domain.repositories

import com.kl3jvi.animity.data.model.AnimeMetaModel
import com.kl3jvi.animity.utils.parser.HtmlParser

interface HomeRepository {
    val parser: HtmlParser
    suspend fun fetchRecentSubOrDub(
        header: Map<String, String>,
        page: Int,
        type: Int
    ): ArrayList<AnimeMetaModel>

    suspend fun fetchPopularFromAjax(
        header: Map<String, String>,
        page: Int
    ): ArrayList<AnimeMetaModel>

    suspend fun fetchNewSeason(header: Map<String, String>, page: Int): ArrayList<AnimeMetaModel>
    suspend fun fetchMovies(header: Map<String, String>, page: Int): ArrayList<AnimeMetaModel>
}