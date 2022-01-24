package com.kl3jvi.animity.data.network

import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

interface AniListService {

//    https://anilist.co/api/v2/oauth/token

    @Headers(
        "Content-Type: application/json",
        "Accept: application/json"
    )
    @POST
    suspend fun getAccessToken(@Url url: String): Response<String>
}