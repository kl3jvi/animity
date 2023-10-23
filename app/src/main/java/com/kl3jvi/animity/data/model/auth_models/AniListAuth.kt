package com.kl3jvi.animity.data.model.auth_models

import com.google.gson.annotations.SerializedName

data class AniListAuth(
    @SerializedName("grant_type")
    val grant_type: String,
    @SerializedName("client_id")
    val client_id: Int,
    @SerializedName("client_secret")
    val client_secret: String,
    @SerializedName("redirect_uri")
    val redirect_uri: String,
    @SerializedName("code")
    val code: String,
)
