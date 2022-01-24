package com.kl3jvi.animity.ui.activities.login

import android.content.Intent
import android.net.Uri

interface Authentication {
    fun getAuthorizationUrl(): Uri
    fun onHandleAuthIntent(intent: Intent?)
//    fun onTokenResponse(response: AccessTokenModel?)
}