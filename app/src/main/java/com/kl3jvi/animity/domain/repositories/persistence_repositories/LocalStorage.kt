package com.kl3jvi.animity.domain.repositories.persistence_repositories

import com.kl3jvi.animity.parsers.Providers


/* It's an interface that defines the properties that are used to store data in the local storage */
interface LocalStorage {
    /* It's a property that is used to store the bearer token in the local storage. */
    var bearerToken: String?
    var refreshToken: String?
    var guestToken: String?
    var aniListUserId: String?

    /* It's a part of the encryption algorithm. */
    var iv: String?
    var key: String?
    var secondKey: String?


    var selectedProvider: Providers?

    /**
     * Clears the storage and logs out
     */
    fun clearStorage()
}