package com.kl3jvi.animity.domain.repositories

import com.kl3jvi.animity.utils.Resource
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    fun getAccessToken(
        code: String,
        clientId: String,
        clientSecret: String,
        redirectUrl: String
    ): Flow<Resource<String>>
}