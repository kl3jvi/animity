package com.kl3jvi.animity.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthenticationInterceptor(
    private var token: String? = null
) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val builder = original.newBuilder()
        val authToken = token

        if (!authToken.isNullOrBlank()) {
            builder.header(
                "Authorization",
                if (authToken.startsWith("Basic")) authToken else "token $authToken"
            )
        }

        val request = builder.build()
        return chain.proceed(request)
    }
}