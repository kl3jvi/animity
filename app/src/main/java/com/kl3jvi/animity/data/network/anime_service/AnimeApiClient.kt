package com.kl3jvi.animity.data.network.anime_service

import com.apollographql.apollo3.ApolloClient
import com.kl3jvi.animity.HomeDataQuery
import javax.inject.Inject

class AnimeApiClient @Inject constructor(
    private val animeService: AnimeService,
    private val apolloClient: ApolloClient
) {
    suspend fun fetchRecentSubOrDub(
        header: Map<String, String>,
        page: Int,
        type: Int
    ) = animeService.fetchRecentSubOrDub(header, page, type)

    suspend fun fetchPopularFromAjax(
        header: Map<String, String>,
        page: Int
    ) = animeService.fetchPopularFromAjax(header, page)

    suspend fun fetchNewSeason(
        header: Map<String, String>,
        page: Int
    ) = animeService.fetchNewestSeason(header, page)

    suspend fun fetchMovies(
        header: Map<String, String>,
        page: Int
    ) = animeService.fetchMovies(header, page)

    suspend fun fetchAnimeInfo(
        header: Map<String, String>,
        episodeUrl: String
    ) = animeService.fetchAnimeInfo(header, episodeUrl)

    suspend fun fetchEpisodeList(
        header: Map<String, String>,
        id: String,
        endEpisode: String,
        alias: String
    ) = animeService.fetchEpisodeList(
        header = header,
        id = id,
        endEpisode = endEpisode,
        alias = alias
    )

    suspend fun fetchEpisodeTimeRelease(episodeUrl: String) =
        animeService.fetchEpisodeTimeRelease(episodeUrl)

    suspend fun fetchEpisodeMediaUrl(header: Map<String, String>, url: String) =
        animeService.fetchEpisodeMediaUrl(header, url)

    suspend fun fetchM3u8Url(header: Map<String, String>, url: String) =
        animeService.fetchM3u8Url(header, url)

    suspend fun fetchSearchData(header: Map<String, String>, keyword: String, page: Int) =
        animeService.fetchSearchData(header, keyword, page)

    suspend fun fetchM3u8PreProcessor(header: Map<String, String>, url: String) =
        animeService.fetchM3u8PreProcessor(header, url)

    suspend fun getGogoUrlFromAniListId(id: Int) = animeService.getGogoUrlFromAniListId(id)

    fun getHomeData() = apolloClient.query(HomeDataQuery()).toFlow()
}
