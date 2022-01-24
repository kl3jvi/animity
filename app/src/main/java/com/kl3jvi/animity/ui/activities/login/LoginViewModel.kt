package com.kl3jvi.animity.ui.activities.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    fun getAccessToken(
        authorizationToken: String,
        anilistId: String,
        anilistSecret: String,
        applicationId: String,
        redirectUri: String
    ) {

    }

}