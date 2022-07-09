package com.kl3jvi.animity.domain.repositories

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.SessionQuery
import com.kl3jvi.animity.ToggleFavouriteMutation
import com.kl3jvi.animity.data.model.ui_models.SessionData
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val bearerToken: String?
    val refreshToken: String?
    val guestToken: String?
    val isAuthenticated: Boolean
    val isGuest: Boolean
    val userId: String?

    fun setBearerToken(authToken: String?)
    fun setRefreshToken(refreshToken: String?)
    fun setAniListUserId(sync: String?)
    fun setProvider(provider: String)
    fun clearStorage()
    fun getSessionForUser(): Flow<ApolloResponse<SessionQuery.Data>>
    fun markAnimeAsFavorite(idAniList: Int?): Flow<ApolloResponse<ToggleFavouriteMutation.Data>>


}