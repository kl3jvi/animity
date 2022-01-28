package com.kl3jvi.animity.data.network.anilist_service

import com.kl3jvi.animity.data.model.auth_models.AniListAuth
import javax.inject.Inject

class AniListClient @Inject constructor(
    private val aniListService: AniListService
) {
    suspend fun getAccessToken(
        grantType: String,
        clientId: Int,
        clientSecret: String,
        redirectUri: String,
        code: String
    ) = aniListService.getAccessToken(
        AniListAuth(
            grant_type = grantType,
            client_id = clientId,
            client_secret = clientSecret,
            redirect_uri = redirectUri,
            code = code
        )
    )

}