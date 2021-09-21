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
}