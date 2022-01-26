package com.kl3jvi.animity.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationInterceptor @Inject constructor() : Interceptor {

    var token: String? = null

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