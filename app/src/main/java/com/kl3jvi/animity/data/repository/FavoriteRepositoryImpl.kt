package com.kl3jvi.animity.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient
import com.kl3jvi.animity.data.network.anime_service.gogo.GogoAnimeApiClient
import com.kl3jvi.animity.data.paging.FavoritesPagingSource
import com.kl3jvi.animity.domain.repositories.FavoriteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class FavoriteRepositoryImpl @Inject constructor(
    private val apiClient: GogoAnimeApiClient,
    private val aniListGraphQlClient: AniListGraphQlClient,
    private val ioDispatcher: CoroutineDispatcher
) : FavoriteRepository {

    override fun getGogoUrlFromAniListId(id: Int) = flow {
        emit(apiClient.getGogoUrlFromAniListId(id).pages?.getGogoUrl().orEmpty())
    }.flowOn(ioDispatcher)

    override fun getFavoriteAnimesFromAniList(
        userId: Int?
    ): Flow<PagingData<AniListMedia>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = true, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { FavoritesPagingSource(aniListGraphQlClient, userId) }
        ).flow.flowOn(ioDispatcher)
    }
    companion object {
        const val NETWORK_PAGE_SIZE = 50
    }
}
