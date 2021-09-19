package com.kl3jvi.animity.view.fragments.home


import androidx.annotation.WorkerThread
import com.kl3jvi.animity.model.network.ApiHelper

class HomeRepository(private val apiHelper: ApiHelper) {

    @WorkerThread
    suspend fun fetchRecentSubOrDub(header: Map<String, String>, page: Int, type: Int) =
        apiHelper.fetchRecentSubOrDub(header, page, type)

}