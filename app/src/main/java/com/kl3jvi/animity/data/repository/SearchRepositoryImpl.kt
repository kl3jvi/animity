package com.kl3jvi.animity.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kl3jvi.animity.data.enums.SortType
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.User
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient
import com.kl3jvi.animity.data.paging.SearchAniListPagingSource
import com.kl3jvi.animity.data.paging.SearchUsersPagingSource
import com.kl3jvi.animity.domain.repositories.SearchRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val apiClient: AniListGraphQlClient,
    private val ioDispatcher: CoroutineDispatcher,
) : SearchRepository {

    override fun fetchAniListSearchData(
        query: String,
        sortType: List<SortType>,
    ): Flow<PagingData<AniListMedia>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = true, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { SearchAniListPagingSource(apiClient, query, sortType) },
        ).flow.flowOn(ioDispatcher)
    }

    override fun fetchAniListUsers(query: String): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = true, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { SearchUsersPagingSource(apiClient, query) },
        ).flow.flowOn(ioDispatcher)
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 50
    }
}
