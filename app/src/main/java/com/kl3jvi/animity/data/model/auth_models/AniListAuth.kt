package com.kl3jvi.animity.data.model.auth_models

import com.google.gson.annotations.SerializedName

/**
 * AniListAuth is a data class with 5 properties.
 * @property {String} grant_type - This is the type of grant you're requesting. For this, it's
 * authorization_code.
 * @property {Int} client_id - The client ID you received from AniList when you registered your app.
 * @property {String} client_secret - This is the secret key that you got from the AniList website.
 * @property {String} redirect_uri - This is the redirect URI that you set up in the AniList developer
 * portal.
 * @property {String} code - The code you received from the user's authorization.
 */
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
    val code: String
)
