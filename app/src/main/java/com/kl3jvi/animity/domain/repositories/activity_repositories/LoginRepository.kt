package com.kl3jvi.animity.domain.repositories.activity_repositories

import com.kl3jvi.animity.data.model.auth_models.AuthResponse
import com.kl3jvi.animity.utils.NetworkResource
import com.kl3jvi.animity.utils.Resource
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    fun getAccessToken(
        grantType: String,
        clientId: Int,
        clientSecret: String,
        redirectUri: String,
        code: String
    ): Flow<NetworkResource<AuthResponse>>
}