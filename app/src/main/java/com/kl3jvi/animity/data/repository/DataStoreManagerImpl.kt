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
    private val dataStore: DataStore<Preferences>
) : DataStoreManager {
    private val AUTH_TOKEN = stringPreferencesKey("authentication_token")
    private val LOGIN_TYPE = stringPreferencesKey("login_type")

    override suspend fun saveTokenToPreferencesStore(token: String) {
        dataStore.edit { preferences ->
            preferences[AUTH_TOKEN] = token
        }
    }

    override fun getTokenFromPreferencesStore(): Flow<String> =
        dataStore.data.map { preference ->
            preference[AUTH_TOKEN] ?: ""
        }

    override suspend fun saveLoginTypeToPreferencesStore(loginType: String) {
        dataStore.edit { preferences ->
            preferences[LOGIN_TYPE] = loginType
        }
    }

    override fun getLoginTypeFromPreferencesStore(): Flow<String> =
        dataStore.data.map { preference ->
            preference[LOGIN_TYPE] ?: ""
        }
}