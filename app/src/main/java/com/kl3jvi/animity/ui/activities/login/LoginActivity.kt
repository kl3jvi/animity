package com.kl3jvi.animity.ui.activities.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.kl3jvi.animity.BuildConfig.*
import com.kl3jvi.animity.R
import com.kl3jvi.animity.data.model.auth_models.AuthResponse
import com.kl3jvi.animity.databinding.ActivityLoginBinding
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.ui.base.BindingActivity
import com.kl3jvi.animity.utils.Constants.Companion.AUTH_GRANT_TYPE
import com.kl3jvi.animity.utils.Constants.Companion.SIGNUP_URL
import com.kl3jvi.animity.utils.Constants.Companion.TERMS_AND_PRIVACY_LINK
import com.kl3jvi.animity.utils.Constants.Companion.showSnack
import com.kl3jvi.animity.utils.NetworkUtils.isConnectedToInternet
import com.kl3jvi.animity.utils.NetworkUtils.unregisterNetworkCallback
import com.kl3jvi.animity.utils.Result
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
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics
        checkIfUserLoggedIn()
        initViews()
    }

    /**
     * It checks if the user is logged in.
     *
     * @return Boolean
     */
    override fun checkIfUserLoggedIn(): Boolean {
        val isLoggedInWithAuth = viewModel.getToken().run { this.isNotNullOrEmpty() }
        if (isLoggedInWithAuth) {
            binding.progressBar.show()
            launchActivity<MainActivity> {
                viewModel.setSelectedProvider("gogoAnime")
            }
            finish()
        }
        return isLoggedInWithAuth
    }

    private fun String?.isNotNullOrEmpty() = !this.isNullOrEmpty()

    /**
     * It returns the authorization url for the user to login.
     *
     * @return A Uri object
     */
    override fun getAuthorizationUrl(): Uri {
        return Uri.Builder().scheme("https")
            .authority("anilist.co")
            .appendPath("api")
            .appendPath("v2")
            .appendPath("oauth")
            .appendPath("authorize")
            .appendQueryParameter("client_id", anilistId)
            .appendQueryParameter("redirect_uri", redirectUri)
            .appendQueryParameter("response_type", "code")
            .build()
    }

    override fun onHandleAuthIntent(intent: Intent?) {
        if (intent != null && intent.data != null) {
            val uri = intent.data
            if (uri.toString().startsWith(redirectUri)) {
                val authorizationToken = uri?.getQueryParameter("code")
                Log.e("AUTH TOKEN", authorizationToken.toString())
                if (!authorizationToken.isNullOrEmpty()) {
                    collectFlow(
                        viewModel.getAccessToken(
                            grantType = AUTH_GRANT_TYPE,
                            clientId = anilistId.toInt(),
                            clientSecret = anilistSecret,
                            redirectUri = redirectUri,
                            authorizationToken = authorizationToken
                        )
                    ) { state ->
                        when (state) {
                            is Result.Error -> showSnack(binding.root, "Error Logging In")
                            Result.Loading -> binding.progressBar.show() // TODO Check this if this raises na issue.
                            is Result.Success -> onTokenResponse(state.data)
                        }
                    }
                }
            }
        }
    }

    /**
     * It saves the tokens and launches the MainActivity.
     *
     * @param response AuthResponse - This is the response object that contains the access token and
     * refresh token.
     * @return The response from the server is being returned.
     */
    override fun onTokenResponse(response: AuthResponse) {
        val authToken: String? = response.accessToken
        val refreshToken: String? = response.refreshToken
        if (!authToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()) {
            viewModel.saveTokens(authToken, refreshToken)
            launchActivity<MainActivity> {
                val bundle = bundleOf(FirebaseAnalytics.Param.METHOD to "login")
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle)
            }
            finish()
            return
        }
        showSnack(binding.root, "Couldn't Login!!")
    }

    private fun initViews() {
        val aniListLogin = binding.aniListLogin
        val aniListRegister = binding.aniListSignUp
        val privacy = binding.privacy

        aniListLogin.launchBrowser(getAuthorizationUrl())
        aniListRegister.launchBrowser(Uri.parse(SIGNUP_URL))
        privacy.launchBrowser(Uri.parse(TERMS_AND_PRIVACY_LINK))
    }


    /**
     * > When the view is clicked, launch a custom tab with the given URI
     *
     * @param uri The URI to be opened in the browser.
     */
    private fun View.launchBrowser(uri: Uri) {
        setOnClickListener {
            CustomTabsIntent.Builder()
                .build()
                .launchUrl(this.context, uri)
        }
    }

    /**
     * It checks if the user is connected to the internet and enables/disables the login and sign up
     * buttons accordingly.
     */
    private fun handleNetworkChanges() {
        isConnectedToInternet(this) { isConnected ->
            if (!isConnected) showSnack(binding.root, "No Internet Connection!")
            binding.aniListSignUp.isEnabled = isConnected
            binding.aniListLogin.isEnabled = isConnected
        }
    }


    /**
     * If the intent is not null, and the intent has the action of the intent filter, then call the
     * handleAuthIntent function
     */
    override fun onResume() {
        super.onResume()
        onHandleAuthIntent(intent)
    }

    /**
     * It handles network changes.
     */
    override fun onStart() {
        super.onStart()
        handleNetworkChanges()
    }

    /**
     * It handles the intent that is passed to the activity.
     *
     * @param intent The intent that was used to start the activity.
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        onHandleAuthIntent(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterNetworkCallback(this)

    }
}