package com.kl3jvi.animity.data.network.interceptor

import android.util.Log
import com.kl3jvi.animity.BuildConfig.anilistId
import com.kl3jvi.animity.BuildConfig.anilistSecret
import com.kl3jvi.animity.analytics.Performance
import com.kl3jvi.animity.data.model.auth_models.AuthResponse
import com.kl3jvi.animity.domain.repositories.LoginRepository
import com.kl3jvi.animity.domain.repositories.PersistenceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor
    @Inject
    constructor(
        private val loginRepository: LoginRepository,
        private val localStorage: PersistenceRepository,
        private val performance: Performance, // Assuming you've added this as a dependency
    ) : Interceptor {
        /**
         * It intercepts the request and adds the bearer token to the header.
         *
         * @param chain Interceptor.Chain - This is the chain of interceptors that the request will go
         * through.
         */
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()

            val token =
                localStorage.bearerToken ?: run {
                    Log.d("HeaderInterceptor", "No bearer token found. Proceeding without it.")
                    return chain.proceed(originalRequest)
                }

            // Adding the header to the request.
            val newRequest =
                originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .build()

            Log.d("HeaderInterceptor", "Added authentication headers to request.")

            val response = chain.proceed(newRequest)

        /* Checking if the response code is 401, if it is, it will get a new access token and refresh
        token and then proceed with the request. */
            if (response.code == 401) {
                Log.w(
                    "HeaderInterceptor",
                    "Received 401 Unauthorized response. Attempting token refresh.",
                )
                localStorage.bearerToken = null

                val refreshToken = localStorage.refreshToken

                if (refreshToken != null) {
                    response.close()

                    val newTokenResponse =
                        performance.measureAndTrace("tokenRefresh") {
                            runBlocking(Dispatchers.IO) {
                                loginRepository.refreshToken(
                                    clientId = anilistId.toInt(),
                                    clientSecret = anilistSecret,
                                    refreshToken = refreshToken,
                                )
                            }.getOrDefault(AuthResponse())
                        }

                    if (newTokenResponse == AuthResponse()) {
                        Log.e(
                            "HeaderInterceptor",
                            "Token refresh failed. Proceeding with original request.",
                        )
                        return chain.proceed(originalRequest)
                    }

                    Log.d(
                        "HeaderInterceptor",
                        "Token refresh successful. Retrying request with new token.",
                    )
                    localStorage.bearerToken = newTokenResponse.accessToken
                    localStorage.refreshToken = newTokenResponse.refreshToken
                    val newAuthorization = "Bearer ${newTokenResponse.accessToken}"
                    val retryRequest =
                        originalRequest.newBuilder()
                            .header("Authorization", newAuthorization)
                            .build()

                    return chain.proceed(retryRequest)
                } else {
                    Log.e(
                        "HeaderInterceptor",
                        "No refresh token found. Unable to refresh access token.",
                    )
                }
            }
            return response
        }
    }
