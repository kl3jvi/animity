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
import com.kl3jvi.animity.data.model.auth_models.AuthResponse
import com.kl3jvi.animity.databinding.ActivityLoginBinding
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.ui.base.BindingActivity
import com.kl3jvi.animity.utils.Constants.Companion.AUTHENTICATED_LOGIN_TYPE
import com.kl3jvi.animity.utils.Constants.Companion.AUTH_GRANT_TYPE
import com.kl3jvi.animity.utils.Constants.Companion.GUEST_LOGIN_TYPE
import com.kl3jvi.animity.utils.Constants.Companion.SIGNUP_URL
import com.kl3jvi.animity.utils.Constants.Companion.TERMS_AND_PRIVACY_LINK
import com.kl3jvi.animity.utils.Constants.Companion.showSnack
import com.kl3jvi.animity.utils.NetworkUtils.isConnectedToInternet
import com.kl3jvi.animity.utils.State
import com.kl3jvi.animity.utils.collectFlow
import com.kl3jvi.animity.utils.launchActivity
import com.kl3jvi.animity.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class LoginActivity : BindingActivity<ActivityLoginBinding>(R.layout.activity_login),
    Authentication {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkIfUserLoggedIn()
        initViews()
    }

    /**
     * Moves to main activity and also returns logged in state to check it
     * when reopening app and not transitioning 2 times to home page after login
     */
    override fun checkIfUserLoggedIn(): Boolean {
        val isLoggedInWithAuth: Boolean
        val isGuestLoggedIn: Boolean
        val authToken = viewModel.getToken()
        val guestToken = viewModel.getGuestToken()
        isLoggedInWithAuth = !authToken.isNullOrEmpty()
        isGuestLoggedIn = !guestToken.isNullOrEmpty()
        if (isLoggedInWithAuth) {
            binding.progressBar.show()
            launchActivity<MainActivity> {}
            finish()
        } else if (isGuestLoggedIn) {
            launchActivity<MainActivity> {
                putExtra("loginType", GUEST_LOGIN_TYPE)
            }
            finish()
        }
        return isLoggedInWithAuth || isGuestLoggedIn
    }


    /**
     * Builds the uri for the authentication url for anilist.co
     */
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
            .appendQueryParameter("client_id", anilistid)
            .appendQueryParameter("redirect_uri", redirecturi)
            .appendQueryParameter("response_type", "code")
            .build()
    }

    override fun onHandleAuthIntent(intent: Intent?) {
        if (intent != null && intent.data != null) {
            val uri = intent.data
            if (uri.toString().startsWith(redirecturi)) {
                val authorizationToken = uri?.getQueryParameter("code")
                Log.e("AUTH TOKEN", authorizationToken.toString())
                if (!authorizationToken.isNullOrEmpty()) {
                    collectFlow(
                        viewModel.getAccessToken(
                            grantType = AUTH_GRANT_TYPE,
                            clientId = anilistid.toInt(),
                            clientSecret = anilistsecret,
                            redirectUri = redirecturi,
                            authorizationToken = authorizationToken
                        )
                    ) { state ->
                        if (state is State.Success) {
                            onTokenResponse(state.data)
                        }
                    }
                }
            }
        }
    }

    override fun onTokenResponse(response: AuthResponse) {
        val token: String = response.accessToken
        Log.e("Token", token)
        if (token.isNotEmpty()) {
            viewModel.saveToken(token)
            launchActivity<MainActivity> {
                putExtra("loginType", AUTHENTICATED_LOGIN_TYPE)
            }
            finish()
            return
        }
//        showSnack(binding.root, "Couldn't Login!!")
    }

    private fun initViews() {
        val aniListLogin = binding.aniListLogin
        val aniListRegister = binding.aniListSignUp
        val guestLogin = binding.guestLogin
        val privacy = binding.privacy

        aniListLogin.setOnClickListener {
            launchBrowser(this@LoginActivity, getAuthorizationUrl())
        }

        aniListRegister.setOnClickListener {
            launchBrowser(this@LoginActivity, Uri.parse(SIGNUP_URL))
        }

        guestLogin.setOnClickListener {
            viewModel.saveGuestToken(GUEST_LOGIN_TYPE)
            launchActivity<MainActivity> {
                putExtra("loginType", GUEST_LOGIN_TYPE)
            }
            finish()
        }

        privacy.setOnClickListener {
            launchBrowser(this@LoginActivity, Uri.parse(TERMS_AND_PRIVACY_LINK))
        }
    }

    private fun launchBrowser(context: Context, uri: Uri) {
        CustomTabsIntent.Builder()
            .build()
            .launchUrl(context, uri)
    }

    private fun handleNetworkChanges() {
        isConnectedToInternet(this) { isConnected ->
            if (!isConnected) showSnack(binding.root, "No Internet Connection!")
            binding.aniListSignUp.isEnabled = isConnected
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