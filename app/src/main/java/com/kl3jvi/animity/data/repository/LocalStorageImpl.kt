package com.kl3jvi.animity.data.repository

import android.content.SharedPreferences
import com.kl3jvi.animity.domain.repositories.persistence_repositories.LocalStorage
import javax.inject.Inject


class LocalStorageImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : LocalStorage {

    companion object {
        private const val BEARER_TOKEN = "bearerToken"
    }

    override var bearerToken: String?
        get() = getData(BEARER_TOKEN)
        set(value) {
            setData(BEARER_TOKEN, value)
        }

    private fun getData(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    private fun setData(key: String, value: String?) {
        sharedPreferences.edit().putString(key, value).apply()
    }
}