package com.kl3jvi.animity.data.repository.fragment_repositories

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.FavoritesAnimeQuery
import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient
import com.kl3jvi.animity.data.network.anime_service.GogoAnimeApiClient
import com.kl3jvi.animity.domain.repositories.fragment_repositories.FavoriteRepository
import com.kl3jvi.animity.utils.logError
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
    ): Flow<List<AniListMedia>> {
        return aniListGraphQlClient.getFavoriteAnimesFromAniList(userId, page)
            .catch { e -> logError(e) }
            .mapNotNull(ApolloResponse<FavoritesAnimeQuery.Data>::convert)

    }
}


