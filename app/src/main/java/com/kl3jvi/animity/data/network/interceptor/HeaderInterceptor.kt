package com.kl3jvi.animity.data.network.interceptor

import com.kl3jvi.animity.BuildConfig.anilistId
import com.kl3jvi.animity.BuildConfig.anilistSecret
import com.kl3jvi.animity.data.model.auth_models.AuthResponse
import com.kl3jvi.animity.domain.repositories.LoginRepository
import com.kl3jvi.animity.domain.repositories.PersistenceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(
    private val loginRepository: LoginRepository,
    private val localStorage: PersistenceRepository
) : Interceptor {

    /**
     * It intercepts the request and adds the bearer token to the header.
     *
     * @param chain Interceptor.Chain - This is the chain of interceptors that the request will go
     * through.
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val token = localStorage.bearerToken ?: return chain.proceed(originalRequest)

        /* Adding the header to the request. */
        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .build()

        val response = chain.proceed(newRequest)

        /* Checking if the response code is 401, if it is, it will get a new access token and refresh
        token and then proceed with the request. */
        if (response.code == 401) {
            localStorage.bearerToken = null

            val refreshToken = localStorage.refreshToken

            if (refreshToken != null) {
                response.close()
                val newTokenResponse = runBlocking(Dispatchers.IO) {
                    loginRepository.refreshtoken(
                        clientId = anilistId.toInt(),
                        clientSecret = anilistSecret,
                        refreshToken = refreshToken
                    )
                }.getOrDefault(AuthResponse())

                if (newTokenResponse == AuthResponse()) {
                    return chain.proceed(originalRequest)
                }

                localStorage.bearerToken = newTokenResponse.accessToken
                localStorage.refreshToken = newTokenResponse.refreshToken
                val newAuthorization = "Bearer ${newTokenResponse.accessToken}"
                val retryRequest = originalRequest.newBuilder()
                    .header("Authorization", newAuthorization)
                    .build()

                return chain.proceed(retryRequest)
            }
        }
        return response
    }
}
