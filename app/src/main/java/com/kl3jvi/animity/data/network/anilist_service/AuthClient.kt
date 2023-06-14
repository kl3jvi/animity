package com.kl3jvi.animity.data.network.anilist_service

import com.kl3jvi.animity.data.model.auth_models.AniListAuth
import com.kl3jvi.animity.data.model.auth_models.AuthResponse
import com.kl3jvi.animity.data.model.auth_models.RefreshTokenRequest
import javax.inject.Inject

class AuthClient @Inject constructor(
    private val aniListService: AniListAuthService
) : Authenticator {
    /**
     * Retrieves an access token from the AniList API.
     *
     * @param grantType The type of grant to use for the request.
     * @param clientId The client ID for the AniList API.
     * @param clientSecret The client secret for the AniList API.
     * @param redirectUri The redirect URI for the AniList API.
     * @param code The authorization code to exchange for an access token.
     * @return A [Result] object containing the [AuthResponse] on success, or the exception on failure.
     */
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

    override suspend fun refreshToken(
        clientId: Int,
        clientSecret: String,
        refreshToken: String
    ): Result<AuthResponse> {
        return runCatching {
            aniListService.refreshToken(
                RefreshTokenRequest(
                    clientId = clientId,
                    clientSecret = clientSecret,
                    refreshToken = refreshToken
                )
            )
        }
    }
}
