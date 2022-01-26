package com.kl3jvi.animity.data.model.auth_models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthResponse(
    @field:Json(name = "access_token")
    val accessToken: String,
    @field:Json(name = "expires_in")
    val expiresIn: Int,
    @field:Json(name = "refresh_token")
    val refreshToken: String,
    @field:Json(name = "token_type")
    val tokenType: String
)