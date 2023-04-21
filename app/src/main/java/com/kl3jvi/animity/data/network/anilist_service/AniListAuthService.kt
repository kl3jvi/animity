package com.kl3jvi.animity.data.network.anilist_service

import com.kl3jvi.animity.data.model.auth_models.AniListAuth
import com.kl3jvi.animity.data.model.auth_models.AuthResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AniListAuthService {
    @Headers(
        "Content-Type: application/json",
        "Accept: application/json"
    )
    @POST(AUTH_URL)
    suspend fun getAccessToken(@Body aniListAuth: AniListAuth): AuthResponse

    companion object {
        const val AUTH_URL = "https://anilist.co/api/v2/oauth/token"
    }
}
