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
            grantType = grantType,
            clientId = clientId,
            clientSecret = clientSecret,
            redirectUrl = redirectUri,
            code = code
        )
    )
}