package com.kl3jvi.animity.data.network.interceptor

import com.kl3jvi.animity.BuildConfig.*
import com.kl3jvi.animity.domain.repositories.LoginRepository
import com.kl3jvi.animity.domain.repositories.PersistenceRepository
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Result
import com.kl3jvi.animity.utils.asResult
import com.kl3jvi.animity.utils.logMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.CountDownLatch
import javax.inject.Inject


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
                var accessToken = ""
                /* A synchronization aid that allows one or more threads to wait until a set of
                operations being performed in other threads completes. */
                val countDownLatch = CountDownLatch(1)
                loginRepository.getAccessToken(
                    grantType = Constants.REFRESH_GRANT_TYPE,
                    clientId = anilistId.toInt(),
                    clientSecret = anilistSecret,
                    redirectUri = redirectUri,
                    code = localStorage.refreshToken.orEmpty()
                ).asResult().collectLatest {
                    when (it) {
                        is Result.Error -> logMessage(it.exception?.message)
                        Result.Loading -> {}
                        is Result.Success -> {
                            accessToken = it.data.accessToken.toString()
                            /* Decrementing the count of the latch, releasing all waiting threads if
                            the count reaches zero. */
                            countDownLatch.countDown()
                        }
                    }
                }
                /* Waiting for the count to reach zero. */
                withContext(Dispatchers.IO) {
                    countDownLatch.await()
                }
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
