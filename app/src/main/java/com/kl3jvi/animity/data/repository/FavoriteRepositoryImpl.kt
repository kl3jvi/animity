package com.kl3jvi.animity.data.repository

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.FavoritesAnimeQuery
import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient
import com.kl3jvi.animity.data.network.anime_service.GogoAnimeApiClient
import com.kl3jvi.animity.domain.repositories.FavoriteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class FavoriteRepositoryImpl @Inject constructor(
    private val apiClient: GogoAnimeApiClient,
    private val aniListGraphQlClient: AniListGraphQlClient,
    private val ioDispatcher: CoroutineDispatcher,
) : FavoriteRepository {


    override fun getGogoUrlFromAniListId(id: Int) = flow {
        emit(apiClient.getGogoUrlFromAniListId(id))
    }.flowOn(ioDispatcher)


    override fun getFavoriteAnimesFromAniList(
        userId: Int?,
        page: Int?
    ) = aniListGraphQlClient.getFavoriteAnimesFromAniList(userId, page)
        .mapNotNull(ApolloResponse<FavoritesAnimeQuery.Data>::convert)
        .flowOn(ioDispatcher)

}


