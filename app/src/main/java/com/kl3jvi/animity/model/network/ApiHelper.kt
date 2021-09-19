package com.kl3jvi.animity.model.network

import androidx.annotation.WorkerThread

class ApiHelper(private val apiService: ApiService) {

    @WorkerThread
    suspend fun fetchRecentSubOrDub(header: Map<String, String>, page: Int, type: Int) =
        apiService.fetchRecentSubOrDub(header, page, type)
}