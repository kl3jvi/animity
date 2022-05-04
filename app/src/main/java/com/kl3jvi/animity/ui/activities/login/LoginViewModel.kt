package com.kl3jvi.animity.ui.activities.login

import androidx.lifecycle.ViewModel
import com.kl3jvi.animity.data.model.auth_models.AuthResponse
import com.kl3jvi.animity.data.repository.fragment_repositories.UserRepositoryImpl
import com.kl3jvi.animity.domain.use_cases.GetAccessTokenUseCase
import com.kl3jvi.animity.utils.State
import com.kl3jvi.animity.utils.mapToState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val userRepository: UserRepositoryImpl
) : ViewModel() {

    /**
     * `getAccessToken` is a function that returns a `Flow` of `State<AuthResponse>`
     *
     * The `Flow` is a stream of `State<AuthResponse>` that can be observed by the `ViewModel`
     *
     * @param grantType The type of grant you are requesting. For this example, we are requesting an
     * authorization code grant.
     * @param clientId The client ID you received from GitHub when you registered.
     * @param clientSecret This is the secret key that you get from the developer portal.
     * @param redirectUri The redirect URI you set in the [developer
     * portal](https://developer.spotify.com/dashboard/applications)
     * @param authorizationToken The authorization token that you received from the user.
     * @return A Flow of State<AuthResponse>
     */
    fun getAccessToken(
        grantType: String,
        clientId: Int,
        clientSecret: String,
        redirectUri: String,
        authorizationToken: String
    ): Flow<State<AuthResponse>> {
        return getAccessTokenUseCase(
            grantType,
            clientId,
            clientSecret,
            redirectUri,
            authorizationToken
        ).mapToState()
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

    fun saveGuestToken(token: String) {
        userRepository.setGuestToken(token = token)
    }

    fun getGuestToken(): String? {
        return userRepository.guestToken
    }
}