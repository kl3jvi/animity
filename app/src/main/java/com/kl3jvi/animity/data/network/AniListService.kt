package com.kl3jvi.animity.data.network

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface AniListService {
    @FormUrlEncoded
    @Headers(
        "Content-Type: application/json",
        "Accept: application/json"
    )
    @POST("https://anilist.co/api/v2/oauth/token")
    suspend fun getAccessToken(
        @Field("grant_type") authorizationCode: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("redirect_uri") redirectUrl: String,
        @Field("code") code: String
    ): Response<String>


}