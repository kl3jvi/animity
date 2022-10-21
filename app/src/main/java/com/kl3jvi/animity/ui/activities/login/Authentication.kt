package com.kl3jvi.animity.ui.activities.login

import android.content.Intent
import android.net.Uri
import com.kl3jvi.animity.data.model.auth_models.AuthResponse

/* The Authentication interface is used to abstract the authentication process. */
interface Authentication {
    fun checkIfUserLoggedIn(): Boolean
    fun getAuthorizationUrl(): Uri
    fun onHandleAuthIntent(intent: Intent?)
    fun onTokenResponse(response: AuthResponse)
}
