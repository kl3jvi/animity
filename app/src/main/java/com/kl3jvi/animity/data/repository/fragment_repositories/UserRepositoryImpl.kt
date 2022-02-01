package com.kl3jvi.animity.data.repository.fragment_repositories

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.kl3jvi.animity.*
import com.kl3jvi.animity.data.repository.persistence_repository.LocalStorageImpl
import com.kl3jvi.animity.domain.repositories.fragment_repositories.UserRepository
import com.kl3jvi.animity.type.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val storage: LocalStorageImpl,
    private val apolloClient: ApolloClient,
) : UserRepository {

    override val bearerToken: String?
        get() = storage.bearerToken

    override val guestToken: String?
        get() = storage.guestToken

    override val isAuthenticated: Boolean
        get() = storage.bearerToken != null

    override val isGuest: Boolean
        get() = storage.guestToken != null


    override fun setBearerToken(token: String?) {
        storage.bearerToken = token
    }

    override fun setGuestToken(token: String?) {
        storage.guestToken = token
    }

    override fun clearStorage() {
        storage.clearStorage()
    }

    override fun getSessionForUser(): Flow<ApolloResponse<SessionQuery.Data>> {
        return try {
            apolloClient.query(SessionQuery()).toFlow()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyFlow<ApolloResponse<SessionQuery.Data>>()
        }

    }

    override fun getUserData(id: Int?): Flow<ApolloResponse<UserQuery.Data>> {
        return try {
            apolloClient.query(UserQuery(Optional.Present(id))).toFlow()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyFlow<ApolloResponse<UserQuery.Data>>()
        }
    }

    override fun getAnimeListData(userId: Int?): Flow<ApolloResponse<AnimeListCollectionQuery.Data>> {
        return try {
            apolloClient.query(AnimeListCollectionQuery(Optional.Present(userId))).toFlow()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyFlow<ApolloResponse<AnimeListCollectionQuery.Data>>()
        }
    }

    override fun getFavoriteAnimes(
        userId: Int?,
        page: Int?
    ): Flow<ApolloResponse<FavoritesAnimeQuery.Data>> {
        return try {
            apolloClient.query(
                FavoritesAnimeQuery(
                    Optional.Present(userId),
                    Optional.Present(page)
                )
            ).toFlow()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyFlow<ApolloResponse<FavoritesAnimeQuery.Data>>()
        }
    }

    override fun getTopTenTrending(): Flow<ApolloResponse<TrendingMediaQuery.Data>> {
        return try {
            apolloClient.query(TrendingMediaQuery()).toFlow()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyFlow<ApolloResponse<TrendingMediaQuery.Data>>()
        }
    }

    override fun markAnimeAsFavorite(animeId: Int?): Flow<ApolloResponse<ToggleFavouriteMutation.Data>> {
        return try {
            apolloClient.mutation(
                ToggleFavouriteMutation(Optional.Present(animeId))
            ).toFlow()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyFlow<ApolloResponse<ToggleFavouriteMutation.Data>>()
        }
    }

    override fun searchAnime(
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
    ): Flow<ApolloResponse<SearchAnimeQuery.Data>> {
        val query = SearchAnimeQuery(
            page = Optional.Present(page),
            search = Optional.presentIfNotNull(search),
            sort = Optional.presentIfNotNull(sort),
            formatIn = Optional.presentIfNotNull(formatIn),
            statusIn = Optional.presentIfNotNull(statusIn),
            sourceIn = Optional.presentIfNotNull(sourceIn),
            countryOfOrigin = Optional.Present(countryOfOrigin),
            season = Optional.Present(season),
            startDateGreater = Optional.Present(startDateGreater),
            startDateLesser = Optional.Present(startDateLesser),
            isAdult = Optional.Present(isAdult),
            onList = Optional.Present(onList),
            genreIn = Optional.presentIfNotNull(genreIn),
            genreNotIn = Optional.presentIfNotNull(genreNotIn),
            minimumTagRank = Optional.Present(minimumTagRank),
            tagIn = Optional.presentIfNotNull(tagIn),
            tagNotIn = Optional.presentIfNotNull(tagNotIn),
            licensedByIn = Optional.presentIfNotNull(licensedByIn),
            episodesGreater = Optional.Present(episodesGreater),
            episodesLesser = Optional.Present(episodesLesser),
            durationGreater = Optional.Present(durationGreater),
            durationLesser = Optional.Present(durationLesser),
            averageScoreGreater = Optional.Present(averageScoreGreater),
            averageScoreLesser = Optional.Present(averageScoreLesser),
            popularityGreater = Optional.Present(popularityGreater),
            popularityLesser = Optional.Present(popularityLesser)
        )
        return apolloClient.query(query).toFlow()
    }
}