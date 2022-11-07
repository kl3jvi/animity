package com.kl3jvi.animity.ui.activities.login

import androidx.lifecycle.ViewModel
import com.kl3jvi.animity.domain.repositories.LoginRepository
import com.kl3jvi.animity.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    fun getAccessToken(
        grantType: String,
        clientId: Int,
        clientSecret: String,
        redirectUri: String,
        authorizationToken: String
    ) = loginRepository.getAccessToken(
        grantType,
        clientId,
        clientSecret,
        redirectUri,
        authorizationToken
    )

    /**
     * > The function `saveTokens` takes two strings, `authToken` and `refreshToken`, and saves them to
     * the `userRepository` object
     *
     * @param authToken The token that is used to authenticate the user.
     * @param refreshToken The refresh token that was returned from the server.
     */
    fun saveTokens(authToken: String, refreshToken: String) {
        userRepository.apply {
            setBearerToken(authToken)
            setRefreshToken(refreshToken)
        }
    }

    /**
     * > The function `getToken()` returns the value of the `bearerToken` property of the
     * `userRepository` object
     *
     * @return The bearer token
     */
    fun getToken(): String? {
        return userRepository.bearerToken
    }

    fun setSelectedProvider(provider: String) {
        userRepository.setProvider(provider = provider)
    }
}
