package com.kl3jvi.animity.ui.activities.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.data.repository.DataStoreManagerImpl
import com.kl3jvi.animity.domain.use_cases.GetAccessTokenUseCase
import com.kl3jvi.animity.utils.State
import com.kl3jvi.animity.utils.mapToState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val store: DataStoreManagerImpl
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

    fun getToken() = store.getTokenFromPreferencesStore()
    fun saveToken(token: String) = viewModelScope.launch {
        store.saveTokenToPreferencesStore(token)
    }
}