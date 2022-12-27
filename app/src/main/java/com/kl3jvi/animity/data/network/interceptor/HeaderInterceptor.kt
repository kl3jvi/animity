package com.kl3jvi.animity.data.network.interceptor

import com.kl3jvi.animity.BuildConfig.anilistId
import com.kl3jvi.animity.BuildConfig.anilistSecret
import com.kl3jvi.animity.BuildConfig.redirectUri
import com.kl3jvi.animity.data.model.auth_models.AuthResponse
import com.kl3jvi.animity.domain.repositories.LoginRepository
import com.kl3jvi.animity.domain.repositories.PersistenceRepository
import com.kl3jvi.animity.utils.Constants
import kotlinx.coroutines.flow.first
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
        /* Adding the header to the request. */
        val response = chain.proceed(
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${localStorage.bearerToken}")
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build()
        )

        /* Checking if the response code is 401, if it is, it will get a new access token and refresh
        token and then proceed with the request. */
        if (response.code != 401) {
            return response
        }

        val accessToken = runBlocking {
            loginRepository.getAccessToken(
                grantType = Constants.REFRESH_GRANT_TYPE,
                clientId = anilistId.toInt(),
                clientSecret = anilistSecret,
                redirectUri = redirectUri,
                code = localStorage.refreshToken.orEmpty()
            ).first()
        }.getOrDefault(AuthResponse())


        /* Adding the new access token to the header and then proceeding with the request. */
        return chain.proceed(
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${accessToken.accessToken}")
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build()
        )
    }
}

