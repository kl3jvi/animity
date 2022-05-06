package com.kl3jvi.animity.domain.repositories.persistence_repositories


interface LocalStorage {
    var bearerToken: String?
    var refreshToken: String?
    var guestToken: String?
    var aniListUserId: String?

    var iv: String?
    var key: String?
    var secondKey: String?

    fun clearStorage()
}