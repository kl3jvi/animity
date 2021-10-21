package com.kl3jvi.animity.model.network

import javax.inject.Inject

class ApiHelper @Inject constructor(private val apiService: AnimeService) {

    suspend fun fetchRecentSubOrDub(header: Map<String, String>, page: Int, type: Int) =
        apiService.fetchRecentSubOrDub(header, page, type)

    suspend fun fetchPopularFromAjax(header: Map<String, String>, page: Int) =
        apiService.fetchPopularFromAjax(header, page)

    suspend fun fetchNewSeason(header: Map<String, String>, page: Int) =
        apiService.fetchNewestSeason(header, page)

    suspend fun fetchMovies(header: Map<String, String>, page: Int) =
        apiService.fetchMovies(header, page)

    suspend fun fetchEpisodeMediaUrl(header: Map<String, String>, url: String) =
        apiService.fetchEpisodeMediaUrl(header, url)

    suspend fun fetchM3u8Url(header: Map<String, String>, url: String) =
        apiService.FetchM3u8Url(header, url)

    suspend fun fetchAnimeInfo(header: Map<String, String>, episodeUrl: String) =
        apiService.fetchAnimeInfo(header, episodeUrl)

    suspend fun fetchEpisodeList(
        header: Map<String, String>,
        id: String,
        endEpisode: String,
        alias: String
    ) =
        apiService.fetchEpisodeList(
            header = header,
            id = id,
            endEpisode = endEpisode,
            alias = alias
        )
}


