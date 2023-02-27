package com.kl3jvi.animity.ui.activities.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.kl3jvi.animity.R
import com.kl3jvi.animity.data.model.auth_models.AuthResponse
import com.kl3jvi.animity.data.secrets.Secrets
import com.kl3jvi.animity.databinding.ActivityLoginBinding
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.utils.Constants.Companion.AUTH_GRANT_TYPE
import com.kl3jvi.animity.utils.Constants.Companion.SIGNUP_URL
import com.kl3jvi.animity.utils.Constants.Companion.TERMS_AND_PRIVACY_LINK
import com.kl3jvi.animity.utils.Constants.Companion.showSnack
import com.kl3jvi.animity.utils.collectFlow
import com.kl3jvi.animity.utils.launchActivity
import com.kl3jvi.animity.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class LoginActivity : AppCompatActivity(R.layout.activity_login), Authentication {

    private val viewModel: LoginViewModel by viewModels()
    lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        Log.e("OAUTH VALUES = ", "$anilistId , $anilistSecret")
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
                        state.onSuccess {
                            it.onTokenResponse()
                        }.onFailure {
                            showSnack(binding.root, "Error Logging In")
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
    override fun AuthResponse.onTokenResponse() {
        val authToken: String? = accessToken
        val refreshToken: String? = refreshToken
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
//        internetConnection(this) { isConnected ->
//            if (!isConnected) showSnack(binding.root, "No Internet Connection!")
//            binding.aniListSignUp.isEnabled = isConnected
//            binding.aniListLogin.isEnabled = isConnected
//        }
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

    companion object {
        val anilistId = Secrets.getAnilistId().orEmpty()
        val redirectUri = Secrets.getRedirectUri().orEmpty()
        val anilistSecret = Secrets.getAnilistSecret().orEmpty()
    }
}
