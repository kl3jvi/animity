package com.kl3jvi.animity.domain.repositories.fragment_repositories

interface UserRepository {
    val bearerToken: String?
    val isLoggedIn: Boolean
    fun setBearerToken(token: String?)
}