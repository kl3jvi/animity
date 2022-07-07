package com.kl3jvi.animity.data.network.anime_service

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.FavoritesAnimeQuery
import com.kl3jvi.animity.data.model.ui_models.DetailedAnimeInfo
import com.kl3jvi.animity.data.model.ui_models.GogoAnimeKeys
import kotlinx.coroutines.flow.Flow

abstract class BaseClient {
    abstract suspend fun getGogoUrlFromAniListId(id: Int): DetailedAnimeInfo
    abstract suspend fun getEncryptionKeys(): GogoAnimeKeys
//    abstract fun getFavoriteAnimesFromAniList(
//        userId: Int?,
//        page: Int?
//    ): Flow<ApolloResponse<FavoritesAnimeQuery.Data>>

}