package com.kl3jvi.animity.repository

import com.kl3jvi.animity.network.AnimeService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor(private val apiHelper: AnimeService) {
    suspend fun fetchSearchData(header: Map<String, String>, keyword: String, page: Int) =
        apiHelper.fetchSearchData(header, keyword, page)
}
