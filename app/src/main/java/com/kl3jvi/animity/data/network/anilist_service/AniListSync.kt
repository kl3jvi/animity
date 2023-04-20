package com.kl3jvi.animity.data.network.anilist_service

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.AnimeListCollectionQuery
import com.kl3jvi.animity.FavoritesAnimeQuery
import com.kl3jvi.animity.HomeDataQuery
import com.kl3jvi.animity.NotificationsQuery
import com.kl3jvi.animity.SearchAnimeQuery
import com.kl3jvi.animity.SessionQuery
import com.kl3jvi.animity.ToggleFavouriteMutation
import com.kl3jvi.animity.TrendingMediaQuery
import com.kl3jvi.animity.UserQuery
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
    suspend fun getNotifications(): ApolloResponse<NotificationsQuery.Data>
}
