package com.kl3jvi.animity.data.network.interceptor

import android.util.Log
import com.kl3jvi.animity.BuildConfig.*
import com.kl3jvi.animity.domain.repositories.activity_repositories.LoginRepository
import com.kl3jvi.animity.domain.repositories.persistence_repositories.LocalStorage
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.NetworkResource
import com.kl3jvi.animity.utils.logMessage
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.CountDownLatch
import javax.inject.Inject


class HeaderInterceptor @Inject constructor(
    private val loginRepository: LoginRepository,
    private val localStorage: LocalStorage
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
                var accessToken = ""
                /* A synchronization aid that allows one or more threads to wait until a set of
                operations being performed in other threads completes. */
                val countDownLatch = CountDownLatch(1)
                loginRepository.getAccessToken(
                    grantType = Constants.REFRESH_GRANT_TYPE,
                    clientId = anilistid.toInt(),
                    clientSecret = anilistsecret,
                    redirectUri = redirecturi,
                    code = localStorage.refreshToken.orEmpty()
                ).collectLatest {
                    when (it) {
                        is NetworkResource.Failed -> logMessage(it.message)
                        is NetworkResource.Success -> {
                            accessToken = it.data.accessToken.toString()
                            /* Decrementing the count of the latch, releasing all waiting threads if
                            the count reaches zero. */
                            countDownLatch.countDown()
                        }
                    }
                }
                /* Waiting for the count to reach zero. */
                countDownLatch.await()
                /* Adding the new access token to the header and then proceeding with the request. */
                proceed(
                    request().newBuilder()
                        .addHeader("Authorization", "Bearer $accessToken")
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json")
                        .build()
                )
            }
        } else response
    }
}