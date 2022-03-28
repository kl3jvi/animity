package com.kl3jvi.animity.data.repository.persistence_repository

import android.content.SharedPreferences
import com.kl3jvi.animity.domain.repositories.persistence_repositories.LocalStorage
import javax.inject.Inject


class LocalStorageImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : LocalStorage {

    companion object {
        private const val BEARER_TOKEN = "bearerToken"
        private const val GUEST_TOKEN = "guestToken"
        private const val FIRST_REMOTE_DATA_SAVE = "saveToDb"
    }

    override var bearerToken: String?
        get() = getData(BEARER_TOKEN)
        set(value) {
            setData(BEARER_TOKEN, value)
        }

    override var guestToken: String?
        get() = getData(GUEST_TOKEN)
        set(value) {
            setData(GUEST_TOKEN, value)
        }

    override var isDataSynced: String?
        get() = getData(FIRST_REMOTE_DATA_SAVE)
        set(value) {
            setData(FIRST_REMOTE_DATA_SAVE, value)
        }

    private fun getData(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    private fun setData(key: String, value: String?) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun clearStorage() {
        sharedPreferences.edit().clear().apply()
    }

}