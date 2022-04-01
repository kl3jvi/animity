package com.kl3jvi.animity.domain.repositories.persistence_repositories

import androidx.lifecycle.LiveData


interface LocalStorage {
    var bearerToken: String?
    var refreshToken: String?
    var guestToken:String?
    var aniListUserId:String?
    fun clearStorage()
}