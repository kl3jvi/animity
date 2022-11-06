package com.kl3jvi.animity.data.network.anilist_service

import com.kl3jvi.animity.data.model.auth_models.AniListAuth
import com.kl3jvi.animity.data.model.auth_models.AuthResponse
import javax.inject.Inject

class AuthClient @Inject constructor(
    private val aniListService: AniListAuthService
) : Authenticator {
    override suspend fun getAccessToken(
        grantType: String,
        clientId: Int,
        clientSecret: String,
        redirectUri: String,
        code: String
    ): Result<AuthResponse> {
        return runCatching {
            aniListService.getAccessToken(
                AniListAuth(
                    grant_type = grantType,
                    client_id = clientId,
                    client_secret = clientSecret,
                    redirect_uri = redirectUri,
                    code = code
                )
            )
        }
    }
}