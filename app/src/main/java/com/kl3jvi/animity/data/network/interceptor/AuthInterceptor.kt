package com.kl3jvi.animity.data.network.interceptor

import com.kl3jvi.animity.data.repository.persistence_repository.LocalStorageImpl
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class HeaderInterceptor @Inject constructor(
    private val localStorage: LocalStorageImpl
) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request().newBuilder()
                .addHeader("Authorization", "Bearer ${localStorage.bearerToken.orEmpty()}")
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build()
        )

    }
}