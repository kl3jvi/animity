package com.kl3jvi.animity.data.repository.persistence_repository

import android.content.SharedPreferences
import com.kl3jvi.animity.domain.repositories.persistence_repositories.LocalStorage
import javax.inject.Inject


class LocalStorageImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : LocalStorage {

    companion object {
        private const val BEARER_TOKEN = "bearerToken"
        private const val REFRESH_TOKEN = "refreshToken"
        private const val GUEST_TOKEN = "guestToken"
        private const val ANILIST_USER_ID = "anilistUserId"
    }

    override var bearerToken: String?
        get() = getData(BEARER_TOKEN)
        set(value) {
            setData(BEARER_TOKEN, value)
        }

    override var refreshToken: String?
        get() = getData(REFRESH_TOKEN)
        set(value) {
            setData(REFRESH_TOKEN, value)
        }

    override var guestToken: String?
        get() = getData(GUEST_TOKEN)
        set(value) {
            setData(GUEST_TOKEN, value)
        }

    override var aniListUserId: String?
        get() = getData(ANILIST_USER_ID)
        set(value) {
            setData(ANILIST_USER_ID, value)
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