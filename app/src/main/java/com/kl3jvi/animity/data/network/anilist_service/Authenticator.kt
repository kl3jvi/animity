package com.kl3jvi.animity.data.network.anilist_service

import com.kl3jvi.animity.data.model.auth_models.AuthResponse

interface Authenticator {
    suspend fun getAccessToken(
        grantType: String,
        clientId: Int,
        clientSecret: String,
        redirectUri: String,
        code: String,
    ): Result<AuthResponse>

    suspend fun refreshToken(
        clientId: Int,
        clientSecret: String,
        refreshToken: String,
    ): Result<AuthResponse>
}
