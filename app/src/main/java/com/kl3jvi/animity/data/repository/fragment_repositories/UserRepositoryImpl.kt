package com.kl3jvi.animity.data.repository.fragment_repositories

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.kl3jvi.animity.*
import com.kl3jvi.animity.data.repository.persistence_repository.LocalStorageImpl
import com.kl3jvi.animity.domain.repositories.fragment_repositories.UserRepository
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

    override val isFavoritesSynced: Boolean
        get() = storage.isDataSynced != null


    override fun setBearerToken(token: String?) {
        storage.bearerToken = token
    }

    override fun setGuestToken(token: String?) {
        storage.guestToken = token
    }

    override fun setSyncData(sync: String?) {
        storage.isDataSynced = sync
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
                ToggleFavouriteMutation(
                    Optional.Present(animeId)
                )
            ).toFlow()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyFlow<ApolloResponse<ToggleFavouriteMutation.Data>>()
        }
    }

    override fun getMediaId(
        query: String?
    ): Flow<ApolloResponse<MediaIdFromNameQuery.Data>> {
        val queryForApollo = MediaIdFromNameQuery(
            search = Optional.Present(query),
        )
        return apolloClient.query(queryForApollo).toFlow()
    }
}