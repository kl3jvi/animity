package com.kl3jvi.animity.ui.activities.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.browser.customtabs.CustomTabsIntent
import com.kl3jvi.animity.BuildConfig.*
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.ActivityLoginBinding
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.ui.base.BindingActivity
import com.kl3jvi.animity.utils.Constants.Companion.AUTH_GRANT_TYPE
import com.kl3jvi.animity.utils.Constants.Companion.GUEST_LOGIN_TYPE
import com.kl3jvi.animity.utils.Constants.Companion.TERMS_AND_PRIVACY_LINK
import com.kl3jvi.animity.utils.Constants.Companion.showSnack
import com.kl3jvi.animity.utils.NetworkUtils
import com.kl3jvi.animity.utils.State
import com.kl3jvi.animity.utils.collectFlow
import com.kl3jvi.animity.utils.launchActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class LoginActivity : BindingActivity<ActivityLoginBinding>(R.layout.activity_login),
    Authentication {

    private lateinit var customTabsIntent: CustomTabsIntent
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
    }

    /**
     * Moves to main activity and also returns logged in state to check it
     * when reopening app and not transitioning 2 times to home page after login
     */
    private fun checkIfUserLoggedIn(): Boolean {
        var isLoggedIn = false
        return isLoggedIn
    }

    override fun getAuthorizationUrl(): Uri {
        /**
         * https://anilist.co/api/v2/oauth/authorize?client_id={client_id}->
         * -> &redirect_uri={redirect_uri}&response_type=token
         */
        return Uri.Builder().scheme("https")
            .authority("anilist.co")
            .appendPath("api")
            .appendPath("v2")
            .appendPath("oauth")
            .appendPath("authorize")
            .appendQueryParameter("client_id", ANILIST_ID)
            .appendQueryParameter("redirect_uri", REDIRECT_URI)
            .appendQueryParameter("response_type", "code")
            .build()
    }

    override fun onHandleAuthIntent(intent: Intent?) {
        if (intent != null && intent.data != null) {
            val uri = intent.data
            if (uri.toString().startsWith(REDIRECT_URI)) {
                val authorizationToken = uri?.getQueryParameter("code")
                Log.e("AUTH TOKEN", authorizationToken.toString())
                if (!authorizationToken.isNullOrEmpty()) {
                    collectFlow(
                        viewModel.getAccessToken(
                            grantType = AUTH_GRANT_TYPE,
                            clientId = ANILIST_ID.toInt(),
                            clientSecret = ANILIST_SECRET,
                            redirectUri = REDIRECT_URI,
                            authorizationToken = authorizationToken
                        )
                    ) { state ->
                        when (state) {
                            is State.Error -> {}
                            is State.Loading -> {}
                            is State.Success -> {
                                Log.e("Auth token", state.data.accessToken)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onTokenResponse(response: String?) {
        if (response != null) {
            val token: String = response
            if (token.isNotEmpty()) {
//                viewModel.saveToken(token) // TODO(save token to shared preferences)
                return
            }
        }
        showSnack(binding.root, "Couldn't Login!!")
    }


    private fun initViews() {
        val aniListLogin = binding.aniListLogin
        val guestLogin = binding.guestLogin
        val privacy = binding.privacy

        aniListLogin.setOnClickListener {
            openInAppBrowser(this@LoginActivity, getAuthorizationUrl())
        }

        guestLogin.setOnClickListener {
            launchActivity<MainActivity> {
                putExtra("GUEST_LOGIN_TYPE", GUEST_LOGIN_TYPE)
            }
            finish()
        }

        privacy.setOnClickListener {
            openInAppBrowser(this@LoginActivity, Uri.parse(TERMS_AND_PRIVACY_LINK))
        }
    }

    private fun openInAppBrowser(context: Context, uri: Uri) {
        customTabsIntent = CustomTabsIntent.Builder().build()
        customTabsIntent.launchUrl(context, uri)
    }

    private fun handleNetworkChanges() {
        NetworkUtils.getNetworkLiveData(applicationContext).observe(this) { isConnected ->
            if (!isConnected) showSnack(binding.root, "No Internet Connection!")
            binding.aniListLogin.isEnabled = isConnected
            binding.guestLogin.isEnabled = isConnected
        }
    }

    override fun onResume() {
        super.onResume()
        onHandleAuthIntent(intent)
    }

    override fun onStart() {
        super.onStart()
        handleNetworkChanges()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        onHandleAuthIntent(intent)
    }
}