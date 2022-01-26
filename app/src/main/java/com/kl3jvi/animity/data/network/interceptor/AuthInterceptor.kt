package com.kl3jvi.animity.data.network.interceptor

import com.kl3jvi.animity.domain.repositories.persistence_repositories.LocalStorage
import okhttp3.Interceptor
import okhttp3.Response


class HeaderInterceptor(private val localStorage: LocalStorage) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request().newBuilder()
                .addHeader("Authorization", "Bearer ${localStorage.bearerToken}")
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build()
        )
    }
}