package com.kl3jvi.animity.view.fragments.home


import com.kl3jvi.animity.model.api.AnimeService
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

    suspend fun fetchSearchData(header: Map<String, String>, keyword: String, page: Int) =
        apiHelper.fetchSearchData(header, keyword, page)

}