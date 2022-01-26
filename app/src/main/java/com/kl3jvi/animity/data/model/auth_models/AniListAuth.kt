package com.kl3jvi.animity.data.model.auth_models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AniListAuth(
    @field:Json(name = "grant_type") val grantType: String,
    @field:Json(name = "client_id") val clientId: Int,
    @field:Json(name = "client_secret") val clientSecret: String,
    @field:Json(name = "redirect_uri") val redirectUrl: String,
    @field:Json(name = "code") val code: String
)
