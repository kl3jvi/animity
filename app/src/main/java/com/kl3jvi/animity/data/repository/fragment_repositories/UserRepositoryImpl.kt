package com.kl3jvi.animity.data.repository.fragment_repositories

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.kl3jvi.animity.AnimeListCollectionQuery
import com.kl3jvi.animity.SessionQuery
import com.kl3jvi.animity.UserQuery
import com.kl3jvi.animity.data.repository.persistence_repository.LocalStorageImpl
import com.kl3jvi.animity.domain.repositories.fragment_repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val storage: LocalStorageImpl,
    private val apolloClient: ApolloClient
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
        return apolloClient.query(SessionQuery()).toFlow()
    }

    override fun getUserData(id: Int?): Flow<ApolloResponse<UserQuery.Data>> {
        return apolloClient.query(UserQuery(Optional.Present(id))).toFlow()
    }

    override fun getAnimeListData(userId: Int?): Flow<ApolloResponse<AnimeListCollectionQuery.Data>> {
        return apolloClient.query(AnimeListCollectionQuery(Optional.Present(userId))).toFlow()
    }
}