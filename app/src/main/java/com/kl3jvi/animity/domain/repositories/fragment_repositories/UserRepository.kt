package com.kl3jvi.animity.domain.repositories.fragment_repositories

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.*
import com.kl3jvi.animity.type.*
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val bearerToken: String?
    val guestToken: String?
    val isAuthenticated: Boolean
    val isGuest: Boolean
    fun setBearerToken(token: String?)
    fun setGuestToken(token: String?)
    fun clearStorage()

    fun getSessionForUser(): Flow<ApolloResponse<SessionQuery.Data>>
    fun getUserData(id: Int?): Flow<ApolloResponse<UserQuery.Data>>
    fun getAnimeListData(userId: Int?): Flow<ApolloResponse<AnimeListCollectionQuery.Data>>
    fun getFavoriteAnimes(userId: Int?, page: Int?): Flow<ApolloResponse<FavoritesAnimeQuery.Data>>
    fun getTopTenTrending(): Flow<ApolloResponse<TrendingMediaQuery.Data>>
    fun markAnimeAsFavorite(animeId: Int?): Flow<ApolloResponse<ToggleFavouriteMutation.Data>>
    fun searchAnime(
        page: Int,
        search: String?,
        sort: List<MediaSort>?,
        formatIn: List<MediaFormat>?,
        statusIn: List<MediaStatus>?,
        sourceIn: List<MediaSource>?,
        countryOfOrigin: String?,
        season: MediaSeason?,
        startDateGreater: Int?,
        startDateLesser: Int?,
        isAdult: Boolean?,
        onList: Boolean?,
        genreIn: List<String>?,
        genreNotIn: List<String>?,
        minimumTagRank: Int?,
        tagIn: List<String>?,
        tagNotIn: List<String>?,
        licensedByIn: List<String>?,
        episodesGreater: Int?,
        episodesLesser: Int?,
        durationGreater: Int?,
        durationLesser: Int?,
        averageScoreGreater: Int?,
        averageScoreLesser: Int?,
        popularityGreater: Int?,
        popularityLesser: Int?
    ): Flow<ApolloResponse<SearchAnimeQuery.Data>>
}