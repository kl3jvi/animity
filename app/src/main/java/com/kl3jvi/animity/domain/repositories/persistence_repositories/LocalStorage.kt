package com.kl3jvi.animity.domain.repositories.persistence_repositories

import androidx.lifecycle.LiveData


interface LocalStorage {
    var bearerToken: String?
    fun clearStorage(): LiveData<Boolean>
}