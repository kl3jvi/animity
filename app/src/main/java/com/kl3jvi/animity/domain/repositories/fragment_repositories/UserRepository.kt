package com.kl3jvi.animity.domain.repositories.fragment_repositories

interface UserRepository {
    val bearerToken: String?
    val guestToken:String?
    val isAuthenticated: Boolean
    val isGuest: Boolean
    fun setBearerToken(token: String?)
    fun setGuestToken(token: String?)
    fun clearStorage()
}