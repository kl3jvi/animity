package com.kl3jvi.animity.data.repository.fragment_repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient
import com.kl3jvi.animity.data.paging.SearchAniListPagingSource
import com.kl3jvi.animity.domain.repositories.fragment_repositories.SearchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val apiClient: AniListGraphQlClient,
    private val ioDispatcher: CoroutineDispatcher
) : SearchRepository {

    override fun fetchAniListSearchData(query: String): Flow<PagingData<AniListMedia>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = true, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { SearchAniListPagingSource(apiClient, query) }
        ).flow.flowOn(ioDispatcher)
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 50
    }

}
