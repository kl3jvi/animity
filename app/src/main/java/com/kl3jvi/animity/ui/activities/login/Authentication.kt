package com.kl3jvi.animity.ui.activities.login

import android.content.Intent
import android.net.Uri
import com.kl3jvi.animity.data.model.auth_models.AuthResponse

interface Authentication {
    fun getAuthorizationUrl(): Uri
    fun onHandleAuthIntent(intent: Intent?)
    fun onTokenResponse(response: AuthResponse)
}