package com.kl3jvi.animity.data.network.interceptor

import com.kl3jvi.animity.BuildConfig.*
import com.kl3jvi.animity.data.repository.persistence_repository.LocalStorageImpl
import com.kl3jvi.animity.domain.repositories.activity_repositories.LoginRepository
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.NetworkResource
import com.kl3jvi.animity.utils.logMessage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import okhttp3.*
import javax.inject.Inject


class HeaderInterceptor @Inject constructor(
    private val localStorage: LocalStorageImpl
) : Interceptor {


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

@ExperimentalCoroutinesApi
class TokenAuthenticatior @Inject constructor(
    private val loginRepository: LoginRepository,
    private val localStorage: LocalStorageImpl
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {

        return runBlocking {
            var accessToken = ""
            loginRepository.getAccessToken(
                grantType = Constants.AUTH_GRANT_TYPE,
                clientId = anilistid.toInt(),
                clientSecret = anilistsecret,
                redirectUri = redirecturi,
                code = localStorage.refreshToken.orEmpty()
            ).collectLatest {
                when (it) {
                    is NetworkResource.Failed -> logMessage(it.message)
                    is NetworkResource.Success -> accessToken = it.data.accessToken.toString()
                }
            }

            response.request.newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build()
        }
    }

}