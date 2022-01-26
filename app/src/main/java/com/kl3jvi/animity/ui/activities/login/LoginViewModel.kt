package com.kl3jvi.animity.ui.activities.login

import androidx.lifecycle.ViewModel
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
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : ViewModel() {
    fun getAccessToken(
        grantType: String,
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        authorizationToken: String
    ): Flow<State<String>> {
        return getAccessTokenUseCase(
            grantType,
            clientId,
            clientSecret,
            redirectUri,
            authorizationToken
        ).mapToState()
    }


}