package com.kl3jvi.animity.model.network

import androidx.annotation.WorkerThread

class ApiHelper(private val apiService: ApiService) {

    @WorkerThread
    suspend fun fetchRecentSubOrDub(header: Map<String, String>, page: Int, type: Int) =
        apiService.fetchRecentSubOrDub(header, page, type)

    @WorkerThread
    suspend fun fetchPopularFromAjax(header: Map<String, String>, page: Int) =
        apiService.fetchPopularFromAjax(header, page)

    @WorkerThread
    suspend fun fetchNewSeason(header: Map<String, String>, page: Int) =
        apiService.fetchNewestSeason(header, page)

    @WorkerThread
    suspend fun fetchMovies(header: Map<String, String>, page: Int) =
        apiService.fetchMovies(header, page)

    @WorkerThread
    suspend fun fetchEpisodeMediaUrl(header: Map<String, String>, url: String) =
        apiService.fetchEpisodeMediaUrl(header, url)

    @WorkerThread
    suspend fun fetchM3u8Url(header: Map<String, String>, url: String) =
        apiService.FetchM3u8Url(header, url)

    @WorkerThread
    suspend fun fetchAnimeInfo(header: Map<String, String>, episodeUrl: String) =
        apiService.fetchAnimeInfo(header, episodeUrl)

    @WorkerThread
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