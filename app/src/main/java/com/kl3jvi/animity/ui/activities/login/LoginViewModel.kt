package com.kl3jvi.animity.ui.activities.login

import androidx.lifecycle.ViewModel
import com.kl3jvi.animity.data.model.auth_models.AuthResponse
import com.kl3jvi.animity.data.repository.fragment_repositories.UserRepositoryImpl
import com.kl3jvi.animity.domain.repositories.activity_repositories.LoginRepository
import com.kl3jvi.animity.domain.repositories.fragment_repositories.UserRepository
import com.kl3jvi.animity.utils.Result
import com.kl3jvi.animity.utils.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val userRepository: UserRepository
) : ViewModel() {


    /**
     * `getAccessToken` is a function that returns a `Flow` of `Result<AuthResponse>`
     *
     * The `Flow` is a stream of `Result<AuthResponse>` that can be observed
     *
     * @param grantType The type of grant you're requesting. For this example, we're using the
     * authorization code grant type.
     * @param clientId The client ID you received from GitHub when you registered.
     * @param clientSecret The client secret for your app.
     * @param redirectUri The redirect URI you set in your app's settings.
     * @param authorizationToken The authorization token that you received from the user.
     * @return A Flow of Result<AuthResponse>
     */
    fun getAccessToken(
        grantType: String,
        clientId: Int,
        clientSecret: String,
        redirectUri: String,
        authorizationToken: String
    ): Flow<Result<AuthResponse>> {
        return loginRepository.getAccessToken(
            grantType,
            clientId,
            clientSecret,
            redirectUri,
            authorizationToken
        ).asResult()
    }

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