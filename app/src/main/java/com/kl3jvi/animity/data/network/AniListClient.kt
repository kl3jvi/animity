package com.kl3jvi.animity.data.network

import javax.inject.Inject

class AniListClient @Inject constructor(
    private val aniListService: AniListService
) {
    suspend fun getAccessToken(
        authenticationCode: String,
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        code: String
    ) = aniListService.getAccessToken(
        authenticationCode,
        clientId,
        clientSecret,
        redirectUri,
        code
    )
}