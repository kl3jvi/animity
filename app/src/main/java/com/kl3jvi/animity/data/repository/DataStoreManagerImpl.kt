package com.kl3jvi.animity.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kl3jvi.animity.domain.repositories.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class DataStoreManagerImpl @Inject constructor(
    private val tokenPreferencesDataStore: DataStore<Preferences>
) : DataStoreManager {
    private val AUTH_TOKEN = stringPreferencesKey("authentication_token")

    override suspend fun saveTokenToPreferencesStore(token: String) {
        tokenPreferencesDataStore.edit { preferences ->
            preferences[AUTH_TOKEN] = token
        }
    }

    override fun getTokenFromPreferencesStore(): Flow<String> =
        tokenPreferencesDataStore.data.map { preference ->
            preference[AUTH_TOKEN] ?: ""
        }
}