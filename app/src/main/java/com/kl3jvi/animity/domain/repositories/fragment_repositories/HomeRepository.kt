package com.kl3jvi.animity.domain.repositories.fragment_repositories

import com.kl3jvi.animity.data.model.AnimeMetaModel
import com.kl3jvi.animity.utils.parser.HtmlParser

interface HomeRepository {
    val parser: HtmlParser
    suspend fun fetchRecentSubOrDub(
        header: Map<String, String>,
        page: Int,
        type: Int
    ): List<AnimeMetaModel>

    suspend fun fetchPopularFromAjax(
        header: Map<String, String>,
        page: Int
    ): List<AnimeMetaModel>

    suspend fun fetchNewSeason(header: Map<String, String>, page: Int): List<AnimeMetaModel>
    suspend fun fetchMovies(header: Map<String, String>, page: Int): List<AnimeMetaModel>
}