package com.kl3jvi.animity.data.model.auth_models

import com.google.gson.annotations.SerializedName


data class AniListAuth(
    @SerializedName("grant_type") val grantType: String,
    @SerializedName("client_id") val clientId: Int,
    @SerializedName("client_secret") val clientSecret: String,
    @SerializedName("redirect_uri") val redirectUrl: String,
    @SerializedName("code") val code: String
)
