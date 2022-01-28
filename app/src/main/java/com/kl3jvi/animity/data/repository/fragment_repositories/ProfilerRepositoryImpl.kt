package com.kl3jvi.animity.data.repository.fragment_repositories

import com.kl3jvi.animity.data.repository.persistence_repository.LocalStorageImpl
import com.kl3jvi.animity.domain.repositories.fragment_repositories.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val storage: LocalStorageImpl
) : UserRepository {

    override val bearerToken: String?
        get() = storage.bearerToken

    override val isLoggedIn: Boolean
        get() = storage.bearerToken != null

    override fun setBearerToken(token: String?) {
        storage.bearerToken = token
    }

}