package com.kl3jvi.animity.data.network.anilist_service

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.*
import com.kl3jvi.animity.data.model.auth_models.AuthResponse
import kotlinx.coroutines.flow.Flow

interface AniListSync {


    fun getHomeData(): Flow<ApolloResponse<HomeDataQuery.Data>>
    fun getProfileData(userId: Int?): Flow<ApolloResponse<UserQuery.Data>>
    fun getAnimeListData(userId: Int?): Flow<ApolloResponse<AnimeListCollectionQuery.Data>>
    fun fetchSearchAniListData(
        query: String,
        page: Int
    ): Flow<ApolloResponse<SearchAnimeQuery.Data>>

    fun getFavoriteAnimesFromAniList(
        userId: Int?,
        page: Int?
    ): Flow<ApolloResponse<FavoritesAnimeQuery.Data>>

    fun markAnimeAsFavorite(animeId: Int?): Flow<ApolloResponse<ToggleFavouriteMutation.Data>>
    fun getTopTenTrending(): Flow<ApolloResponse<TrendingMediaQuery.Data>>
    fun getFavoriteAnimes(userId: Int?, page: Int?): Flow<ApolloResponse<FavoritesAnimeQuery.Data>>
    fun getUserData(id: Int?): Flow<ApolloResponse<UserQuery.Data>>
    fun getSessionForUser(): Flow<ApolloResponse<SessionQuery.Data>>
}
