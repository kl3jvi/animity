package com.kl3jvi.animity.view.fragments.home


import androidx.annotation.WorkerThread
import com.kl3jvi.animity.model.network.ApiHelper

class HomeRepository(private val apiHelper: ApiHelper) {

    @WorkerThread
    suspend fun fetchRecentSubOrDub(header: Map<String, String>, page: Int, type: Int) =
        apiHelper.fetchRecentSubOrDub(header, page, type)

    @WorkerThread
    suspend fun fetchPopularFromAjax(header: Map<String, String>, page: Int) =
        apiHelper.fetchPopularFromAjax(header, page)

    @WorkerThread
    suspend fun fetchNewSeason(header: Map<String, String>, page: Int) =
        apiHelper.fetchNewSeason(header, page)

    @WorkerThread
    suspend fun fetchMovies(header: Map<String, String>, page: Int) =
        apiHelper.fetchMovies(header, page)



}