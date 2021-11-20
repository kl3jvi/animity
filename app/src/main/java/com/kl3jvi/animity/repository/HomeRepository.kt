package com.kl3jvi.animity.repository

import com.kl3jvi.animity.network.AnimeService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(private val apiHelper: AnimeService) {

    suspend fun fetchRecentSubOrDub(header: Map<String, String>, page: Int, type: Int) =
        apiHelper.fetchRecentSubOrDub(header, page, type)

    suspend fun fetchPopularFromAjax(header: Map<String, String>, page: Int) =
        apiHelper.fetchPopularFromAjax(header, page)

    suspend fun fetchNewSeason(header: Map<String, String>, page: Int) =
        apiHelper.fetchNewestSeason(header, page)

    suspend fun fetchMovies(header: Map<String, String>, page: Int) =
        apiHelper.fetchMovies(header, page)
}
