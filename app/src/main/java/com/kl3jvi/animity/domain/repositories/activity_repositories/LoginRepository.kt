package com.kl3jvi.animity.domain.repositories.activity_repositories

import com.kl3jvi.animity.utils.NetworkResource
import com.kl3jvi.animity.utils.Resource
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    fun getAccessToken(
        authenticationCode: String,
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        code: String
    ): Flow<NetworkResource<String>>
}