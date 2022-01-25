package com.kl3jvi.animity.ui.activities.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.data.repository.DataStoreManagerImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStore: DataStoreManagerImpl
) : ViewModel() {
    fun getAccessToken(
        grantType: String,
        anilistId: String,
        anilistSecret: String,
        redirectUri: String,
        authorizationToken: String
    ) = flow<String> { }

    fun saveLoginType(loginType: String) = viewModelScope.launch {
        dataStore.saveLoginTypeToPreferencesStore(loginType)
    }
}