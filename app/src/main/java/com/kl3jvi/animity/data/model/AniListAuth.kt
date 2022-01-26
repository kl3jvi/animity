package com.kl3jvi.animity.data.model

data class AniListAuth(
    val grantType: String,
    val clientId: String,
    val clientSecret: String,
    val redirectUrl: String,
    val code: String
)
