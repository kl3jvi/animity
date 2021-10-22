package com.kl3jvi.animity.view.fragments.search

import com.kl3jvi.animity.model.api.AnimeService
import javax.inject.Inject

class SearchRepository @Inject constructor(private val apiHelper: AnimeService) {

    suspend fun fetchSearchData(header: Map<String, String>, keyword: String, page: Int) =
        apiHelper.fetchSearchData(header, keyword, page)

}