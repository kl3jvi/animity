package com.kl3jvi.animity.domain.repositories.persistence_repositories


import kotlinx.coroutines.flow.Flow

interface DataStoreManager {
    suspend fun saveTokenToPreferencesStore(token: String)
    fun getTokenFromPreferencesStore(): Flow<String>
}