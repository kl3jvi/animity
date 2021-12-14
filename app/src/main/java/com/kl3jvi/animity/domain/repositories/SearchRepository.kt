package com.kl3jvi.animity.domain.repositories

import com.kl3jvi.animity.model.AnimeMetaModel
import com.kl3jvi.animity.utils.parser.HtmlParser

interface SearchRepository {
    val parser: HtmlParser
    suspend fun fetchSearchData(
        header: Map<String, String>,
        keyword: String,
        page: Int
    ): ArrayList<AnimeMetaModel>
}