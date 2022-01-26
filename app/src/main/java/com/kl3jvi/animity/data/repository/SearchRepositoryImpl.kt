package com.kl3jvi.animity.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kl3jvi.animity.data.paging.SearchPagingSource
import com.kl3jvi.animity.data.model.AnimeMetaModel
import com.kl3jvi.animity.data.network.anime_service.AnimeApiClient
import com.kl3jvi.animity.domain.repositories.fragment_repositories.SearchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Suppress("BlockingMethodInNonBlockingContext")
class SearchRepositoryImpl @Inject constructor(
    private val apiClient: AnimeApiClient,
    private val ioDispatcher: CoroutineDispatcher
) : SearchRepository {

    override fun fetchSearchData(
        header: Map<String, String>,
        keyword: String,
    ): Flow<PagingData<AnimeMetaModel>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = true, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { SearchPagingSource(apiClient, keyword) }
        ).flow.flowOn(ioDispatcher)
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }

}
