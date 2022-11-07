package com.kl3jvi.animity.data.network.interceptor

import com.kl3jvi.animity.BuildConfig.anilistId
import com.kl3jvi.animity.BuildConfig.anilistSecret
import com.kl3jvi.animity.BuildConfig.redirectUri
import com.kl3jvi.animity.domain.repositories.LoginRepository
import com.kl3jvi.animity.domain.repositories.PersistenceRepository
import com.kl3jvi.animity.utils.Constants
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine


class HeaderInterceptor @Inject constructor(
    private val loginRepository: LoginRepository,
    private val localStorage: PersistenceRepository,
) : Interceptor {

    /**
     * It intercepts the request and adds the bearer token to the header.
     *
     * @param chain Interceptor.Chain - This is the chain of interceptors that the request will go
     * through.
     */
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {

        /* Adding the header to the request. */
        val response = proceed(
            request().newBuilder()
                .addHeader("Authorization", "Bearer ${localStorage.bearerToken}")
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build()
        )


        /* Checking if the response code is 401, if it is, it will get a new access token and refresh
        token and then proceed with the request. */
        if (response.code == 401) {
            runBlocking {
                val accessToken = suspendCoroutine { continuation ->
                    this.launch {
                        loginRepository.getAccessToken(
                            grantType = Constants.REFRESH_GRANT_TYPE,
                            clientId = anilistId.toInt(),
                            clientSecret = anilistSecret,
                            redirectUri = redirectUri,
                            code = localStorage.refreshToken.orEmpty()
                        ).collect { response ->
                            continuation.resumeWith(response)
                        }
                    }
                }


                /* A synchronization aid that allows one or more threads to wait until a set of
                operations being performed in other threads completes. */


                /* Adding the new access token to the header and then proceeding with the request. */
                proceed(
                    request().newBuilder()
                        .addHeader("Authorization", "Bearer ${accessToken.accessToken}")
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json")
                        .build()
                )
            }
        } else response

    }
}


