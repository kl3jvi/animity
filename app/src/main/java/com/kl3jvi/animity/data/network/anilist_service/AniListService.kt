package com.kl3jvi.animity.data.network.anilist_service

import com.kl3jvi.animity.data.model.auth_models.AniListAuth
import com.kl3jvi.animity.data.model.auth_models.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AniListService {

    @Headers(
        "Content-Type: application/json",
        "Accept: application/json"
    )
    @POST("https://anilist.co/api/v2/oauth/token")
    suspend fun getAccessToken(@Body aniListAuth: AniListAuth): Response<AuthResponse>


}