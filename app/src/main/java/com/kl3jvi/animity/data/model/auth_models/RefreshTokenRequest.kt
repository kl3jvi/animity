package com.kl3jvi.animity.data.model.auth_models

import com.google.gson.annotations.SerializedName

data class RefreshTokenRequest(
    @SerializedName("grant_type")
    val grantType: String = "refresh_token",
    @SerializedName("client_id")
    val clientId: Int,
    @SerializedName("client_secret")
    val clientSecret: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
)
