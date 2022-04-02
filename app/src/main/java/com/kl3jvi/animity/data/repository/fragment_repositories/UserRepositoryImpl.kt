package com.kl3jvi.animity.data.repository.fragment_repositories

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.kl3jvi.animity.*
import com.kl3jvi.animity.data.repository.persistence_repository.LocalStorageImpl
import com.kl3jvi.animity.domain.repositories.fragment_repositories.UserRepository
import com.kl3jvi.animity.utils.logError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val storage: LocalStorageImpl,
    private val apolloClient: ApolloClient,
) : UserRepository {

    override val bearerToken: String?
        get() = storage.bearerToken

    override val refreshToken: String?
        get() = storage.refreshToken

    override val guestToken: String?
        get() = storage.guestToken

    override val isAuthenticated: Boolean
        get() = storage.bearerToken != null

    override val isGuest: Boolean
        get() = storage.guestToken != null

    override val userId: String?
        get() = storage.aniListUserId


    override fun setBearerToken(authToken: String?) {
        storage.bearerToken = authToken
    }

    override fun setRefreshToken(refreshToken: String?) {
        storage.refreshToken = refreshToken
    }

    override fun setGuestToken(token: String?) {
        storage.guestToken = token
    }

    override fun setAniListUserId(sync: String?) {
        storage.aniListUserId = sync
    }

    override fun clearStorage() {
        storage.clearStorage()
    }

    override fun getSessionForUser(): Flow<ApolloResponse<SessionQuery.Data>> {
        return try {
            apolloClient.query(SessionQuery()).toFlow()
        } catch (e: Exception) {
            logError(e)
            emptyFlow<ApolloResponse<SessionQuery.Data>>()
        }

    }

    override fun getUserData(id: Int?): Flow<ApolloResponse<UserQuery.Data>> {
        return try {
            apolloClient.query(UserQuery(Optional.Present(id))).toFlow()
        } catch (e: Exception) {
            logError(e)
            emptyFlow<ApolloResponse<UserQuery.Data>>()
        }
    }

    override fun getAnimeListData(userId: Int?): Flow<ApolloResponse<AnimeListCollectionQuery.Data>> {
        return try {
            apolloClient.query(AnimeListCollectionQuery(Optional.Present(userId))).toFlow()
        } catch (e: Exception) {
            logError(e)
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
            logError(e)
            emptyFlow<ApolloResponse<FavoritesAnimeQuery.Data>>()
        }
    }

    override fun getTopTenTrending(): Flow<ApolloResponse<TrendingMediaQuery.Data>> {
        return try {
            apolloClient.query(TrendingMediaQuery()).toFlow()
        } catch (e: Exception) {
            logError(e)
            emptyFlow<ApolloResponse<TrendingMediaQuery.Data>>()
        }
    }

    override suspend fun markAnimeAsFavorite(animeId: Int?): Flow<ApolloResponse<ToggleFavouriteMutation.Data>> {
        return try {
            withContext(Dispatchers.IO) {
                apolloClient.mutation(
                    ToggleFavouriteMutation(
                        Optional.Present(animeId)
                    )
                ).toFlow()
            }
        } catch (e: Exception) {
            logError(e)
            emptyFlow<ApolloResponse<ToggleFavouriteMutation.Data>>()
        }
    }


}