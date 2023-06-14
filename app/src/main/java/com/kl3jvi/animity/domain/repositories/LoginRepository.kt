package com.kl3jvi.animity.domain.repositories

import com.kl3jvi.animity.data.model.auth_models.AuthResponse
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    fun getAccessToken(
        grantType: String,
        clientId: Int,
        clientSecret: String,
        redirectUri: String,
        code: String
    ): Flow<Result<AuthResponse>>

    suspend fun refreshtoken(
        clientId: Int,
        clientSecret: String,
        refreshToken: String
    ): Result<AuthResponse>
}
