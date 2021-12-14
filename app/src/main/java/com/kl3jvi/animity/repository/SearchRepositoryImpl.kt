package com.kl3jvi.animity.repository

import com.kl3jvi.animity.domain.repositories.SearchRepository
import com.kl3jvi.animity.model.AnimeMetaModel
import com.kl3jvi.animity.network.AnimeApiClient
import com.kl3jvi.animity.utils.Constants.Companion.TYPE_SEARCH
import com.kl3jvi.animity.utils.parser.HtmlParser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val apiClient: AnimeApiClient
) : SearchRepository {
    override val parser: HtmlParser
        get() = HtmlParser

    override suspend fun fetchSearchData(
        header: Map<String, String>,
        keyword: String,
        page: Int
    ): ArrayList<AnimeMetaModel> {
        return parser.parseMovie(
            apiClient.fetchSearchData(header, keyword, page).string(),
            TYPE_SEARCH
        )
    }
}
