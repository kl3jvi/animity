package com.kl3jvi.animity.data.repository

import com.kl3jvi.animity.data.model.AnimeMetaModel
import com.kl3jvi.animity.data.network.AnimeApiClient
import com.kl3jvi.animity.domain.repositories.SearchRepository
import com.kl3jvi.animity.utils.Constants.Companion.TYPE_SEARCH
import com.kl3jvi.animity.utils.parser.HtmlParser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Suppress("BlockingMethodInNonBlockingContext")
class SearchRepositoryImpl @Inject constructor(
    private val apiClient: AnimeApiClient,
    private val ioDispatcher: CoroutineDispatcher
) : SearchRepository {
    override val parser: HtmlParser
        get() = HtmlParser

    override suspend fun fetchSearchData(
        header: Map<String, String>,
        keyword: String,
        page: Int
    ): List<AnimeMetaModel> = withContext(ioDispatcher) {
        parser.parseMovie(
            apiClient.fetchSearchData(
                header = header,
                keyword = keyword,
                page = page
            ).string(),
            TYPE_SEARCH
        )
    }
}
