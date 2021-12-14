package com.kl3jvi.animity.repository

import com.kl3jvi.animity.domain.repositories.HomeRepository
import com.kl3jvi.animity.model.AnimeMetaModel
import com.kl3jvi.animity.network.AnimeApiClient
import com.kl3jvi.animity.utils.Constants.Companion.TYPE_MOVIE
import com.kl3jvi.animity.utils.Constants.Companion.TYPE_POPULAR_ANIME
import com.kl3jvi.animity.utils.Constants.Companion.TYPE_RECENT_SUB
import com.kl3jvi.animity.utils.parser.HtmlParser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepositoryImpl @Inject constructor(private val apiClient: AnimeApiClient) :
    HomeRepository {
    override val parser: HtmlParser
        get() = HtmlParser

    override suspend fun fetchRecentSubOrDub(
        header: Map<String, String>,
        page: Int,
        type: Int
    ): ArrayList<AnimeMetaModel> {
        return parser.parseRecentSubOrDub(
            apiClient.fetchRecentSubOrDub(header, page, type).string(), TYPE_RECENT_SUB
        )
    }

    override suspend fun fetchPopularFromAjax(
        header: Map<String, String>,
        page: Int
    ): ArrayList<AnimeMetaModel> {
        return parser.parsePopular(apiClient.fetchPopularFromAjax(header, page).string(), TYPE_POPULAR_ANIME)
    }

    override suspend fun fetchNewSeason(
        header: Map<String, String>,
        page: Int
    ): ArrayList<AnimeMetaModel> {
        return parser.parseMovie(apiClient.fetchNewSeason(header, page).string(),TYPE_MOVIE)
    }

    override suspend fun fetchMovies(
        header: Map<String, String>,
        page: Int
    ): ArrayList<AnimeMetaModel> {
        return parser.parseMovie(apiClient.fetchMovies(header, page).string(),TYPE_MOVIE)
    }
}
