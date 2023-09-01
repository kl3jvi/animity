package com.kl3jvi.animity.data.network.anilist_service

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.AiringQuery
import com.kl3jvi.animity.AnimeListCollectionQuery
import com.kl3jvi.animity.FavoritesAnimeQuery
import com.kl3jvi.animity.GetFollowersListQuery
import com.kl3jvi.animity.HomeDataQuery
import com.kl3jvi.animity.NotificationsQuery
import com.kl3jvi.animity.SaveMediaListEntryMutation
import com.kl3jvi.animity.SaveMediaMutation
import com.kl3jvi.animity.SearchAnimeQuery
import com.kl3jvi.animity.SearchUsersQuery
import com.kl3jvi.animity.SendMessageMutation
import com.kl3jvi.animity.SessionQuery
import com.kl3jvi.animity.ToggleFavouriteMutation
import com.kl3jvi.animity.ToggleFollowUserMutation
import com.kl3jvi.animity.TrendingMediaQuery
import com.kl3jvi.animity.UserQuery
import com.kl3jvi.animity.type.MediaListStatus
import com.kl3jvi.animity.type.MediaSort

interface AniListSync {

    suspend fun getUserDataById(userId: Int?): ApolloResponse<UserQuery.Data>
    suspend fun getAnimeListData(userId: Int?): ApolloResponse<AnimeListCollectionQuery.Data>
    suspend fun fetchSearchAniListData(
        query: String,
        page: Int,
        toMediaSort: List<MediaSort>,
    ): ApolloResponse<SearchAnimeQuery.Data>

    suspend fun markAnimeAsFavorite(animeId: Int?): ApolloResponse<ToggleFavouriteMutation.Data>
    suspend fun getTopTenTrending(): ApolloResponse<TrendingMediaQuery.Data>
    suspend fun getFavoriteAnimes(
        userId: Int?,
        page: Int?,
    ): ApolloResponse<FavoritesAnimeQuery.Data>

    suspend fun getUserData(id: Int?): ApolloResponse<UserQuery.Data>
    suspend fun getSessionForUser(): ApolloResponse<SessionQuery.Data>
    suspend fun getNotifications(page: Int): ApolloResponse<NotificationsQuery.Data>
    suspend fun markAnimeStatus(
        mediaId: Int,
        status: MediaListStatus,
    ): ApolloResponse<SaveMediaMutation.Data>

    suspend fun markWatchedEpisode(
        mediaId: Int,
        episodesWatched: Int,
    ): ApolloResponse<SaveMediaListEntryMutation.Data>

    suspend fun sendMessage(
        recipientId: Int,
        message: String,
        parentId: Int?,
    ): ApolloResponse<SendMessageMutation.Data>

    suspend fun getHomeData(): ApolloResponse<HomeDataQuery.Data>
    suspend fun followUser(id: Int): ApolloResponse<ToggleFollowUserMutation.Data>
    suspend fun fetchUsers(query: String, page: Int): ApolloResponse<SearchUsersQuery.Data>
    suspend fun getFollowersAndFollowing(page: Int): ApolloResponse<GetFollowersListQuery.Data>
    suspend fun getAiringAnimeForDate(startDate: Int?, endDate: Int?): ApolloResponse<AiringQuery.Data>
}
