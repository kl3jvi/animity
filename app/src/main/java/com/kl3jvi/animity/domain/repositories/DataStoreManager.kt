package com.kl3jvi.animity.domain.repositories


import kotlinx.coroutines.flow.Flow

interface DataStoreManager {
    suspend fun saveTokenToPreferencesStore(token: String)
    fun getTokenFromPreferencesStore(): Flow<String>

    suspend fun saveLoginTypeToPreferencesStore(loginType: String)
    fun getLoginTypeFromPreferencesStore(): Flow<String>
}