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
    private val userRepository: UserRepository,
) : ViewModel() {

    fun getAccessToken(
        grantType: String,
        clientId: Int,
        clientSecret: String,
        redirectUri: String,
        authorizationToken: String,
    ) = loginRepository.getAccessToken(
        grantType,
        clientId,
        clientSecret,
        redirectUri,
        authorizationToken,
    )

    fun saveTokens(
        authToken: String,
        refreshToken: String,
        expiration: Int,
    ) = with(userRepository) {
        setBearerToken(authToken)
        setRefreshToken(refreshToken)
        setExpirationTime(expiration)
    }

    fun getToken(): String? = userRepository.bearerToken
}
