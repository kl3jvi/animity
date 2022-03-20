package com.kl3jvi.animity.domain.repositories.fragment_repositories

import com.kl3jvi.animity.ui.adapters.homeAdapter.HomeRecyclerViewItem
import com.kl3jvi.animity.utils.parser.HtmlParser

interface HomeRepository {
    val parser: HtmlParser
    suspend fun fetchRecentSubOrDub(
        header: Map<String, String>,
        page: Int,
        type: Int
    ): HomeRecyclerViewItem.HorizontalAnimeWrapper

    suspend fun fetchPopularFromAjax(
        header: Map<String, String>,
        page: Int
    ): List<HomeRecyclerViewItem.Anime>

    suspend fun fetchNewSeason(
        header: Map<String, String>,
        page: Int
    ): HomeRecyclerViewItem.HorizontalAnimeWrapper

    suspend fun fetchMovies(
        header: Map<String, String>,
        page: Int
    ): HomeRecyclerViewItem.HorizontalAnimeWrapper
}