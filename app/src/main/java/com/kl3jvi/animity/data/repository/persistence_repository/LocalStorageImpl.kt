package com.kl3jvi.animity.data.repository.persistence_repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.kl3jvi.animity.domain.repositories.persistence_repositories.LocalStorage
import com.kl3jvi.animity.parsers.Providers
import javax.inject.Inject


class LocalStorageImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : LocalStorage {

    companion object {
        private const val BEARER_TOKEN = "bearerToken"
        private const val REFRESH_TOKEN = "refreshToken"
        private const val GUEST_TOKEN = "guestToken"
        private const val ANILIST_USER_ID = "anilistUserId"
        const val SELECTED_PROVIDER = "selectedProvider"
        const val SELECTED_DNS = "dns"

        private const val ORIGIN = "https://gogoanime.gg/"
        private const val REFERER = "https://goload.pro/"
        private const val BASE_URL = "https://gogoanime.gg"
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

    override var dns: Int?
        get() = getData(SELECTED_DNS)?.toInt()
        set(value) {
            setData(SELECTED_DNS, value.toString())
        }

    override var selectedProvider: Providers?
        get() = getData(SELECTED_PROVIDER)?.fromJson<Providers>()
        set(value) {
            setData(SELECTED_PROVIDER, value.toJson())
        }


    override var iv: String?
        get() = getData(ORIGIN)
        set(value) {
            setData(ORIGIN, value)
        }

    override var key: String?
        get() = getData(REFERER)
        set(value) {
            setData(REFERER, value)
        }

    override var secondKey: String?
        get() = getData(BASE_URL)
        set(value) {
            setData(BASE_URL, value)
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

inline fun <reified T> String?.fromJson(): T {
    return Gson().fromJson(this.orEmpty(), T::class.java)
}

inline fun <reified T> T?.toJson(): String? {
    return Gson().toJson(this)
}

