package com.kl3jvi.animity.data.repository.fragment_repositories

import com.kl3jvi.animity.data.repository.persistence_repository.LocalStorageImpl
import com.kl3jvi.animity.domain.repositories.fragment_repositories.ProfileRepository
import javax.inject.Inject

class ProfilerRepositoryImpl @Inject constructor(
    private val storage: LocalStorageImpl
) : ProfileRepository {

    override val isLoggedIn: Boolean
        get() = storage.bearerToken != null

}