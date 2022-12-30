package com.kl3jvi.animity.data.model.auth_models

import com.google.gson.annotations.SerializedName

/**
 * It's a data class that has four properties, three of which are nullable.
 * @property {String?} accessToken - The access token that you'll use to make authenticated requests.
 * @property {Int?} expiresIn - The number of seconds until the access token expires.
 * @property {String?} refreshToken - This is the token that you will use to refresh the access token
 * when it expires.
 * @property {String?} tokenType - The type of token returned. At this time, this field will always
 * have the value Bearer.
 */
data class AuthResponse(
    @SerializedName("access_token")
    val accessToken: String? = "",
    @SerializedName("expires_in")
    val expiresIn: Int? = 1,
    @SerializedName("refresh_token")
    var refreshToken: String? = "",
    @SerializedName("token_type")
    val tokenType: String? = ""
) {
    fun isExpired(): Boolean {
        return expiresIn?.let {
            System.currentTimeMillis() > it
        } ?: false
    }
}
