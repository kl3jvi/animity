package com.kl3jvi.animity.ui.activities.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.kl3jvi.animity.R
import com.kl3jvi.animity.analytics.Analytics
import com.kl3jvi.animity.data.model.auth_models.AuthResponse
import com.kl3jvi.animity.data.secrets.Secrets
import com.kl3jvi.animity.databinding.ActivityLoginBinding
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.utils.Constants.Companion.showSnack
import com.kl3jvi.animity.utils.collect
import com.kl3jvi.animity.utils.launchActivity
import com.kl3jvi.animity.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class LoginActivity : AppCompatActivity(R.layout.activity_login), Authentication {

    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var firebaseAnalytics: Analytics

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            launchActivity<MainActivity> {}
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
            .appendQueryParameter("client_id", aniListId)
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
                    collect(
                        viewModel.getAccessToken(
                            grantType = AUTH_GRANT_TYPE,
                            clientId = aniListId.toInt(),
                            clientSecret = aniListSecret,
                            redirectUri = redirectUri,
                            authorizationToken = authorizationToken
                        )
                    ) { state ->
                        state.onSuccess {
                            it.onTokenResponse()
                        }.onFailure {
                            showSnack(binding.root, "Error Logging In! Reason: ${it.message}")
                        }
                    }
                }
            }
        }
    }

    override fun AuthResponse.onTokenResponse() {
        val authToken: String? = accessToken
        val refreshToken: String? = refreshToken
        val expiration: Int? = expiresIn
        if (!authToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty() && expiration != null) {
            viewModel.saveTokens(authToken, refreshToken, expiration)
            launchActivity<MainActivity> {
                val params = mapOf(FirebaseAnalytics.Param.METHOD to "login")
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, params)

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
    private fun View.launchBrowser(uri: Uri) = setOnClickListener {
        CustomTabsIntent.Builder()
            .build()
            .launchUrl(this.context, uri)
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
     * It handles the intent that is passed to the activity.
     *
     * @param intent The intent that was used to start the activity.
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        onHandleAuthIntent(intent)
    }

    companion object {
        val aniListId = Secrets.aniListId
        val aniListSecret = Secrets.aniListSecret
        val redirectUri = Secrets.redirectUri

        const val AUTH_GRANT_TYPE = "authorization_code"
        const val SIGNUP_URL = "https://anilist.co/signup"
        const val TERMS_AND_PRIVACY_LINK = "https://anilist.co/terms"
    }
}
