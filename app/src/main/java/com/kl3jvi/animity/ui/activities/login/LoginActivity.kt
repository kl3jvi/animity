package com.kl3jvi.animity.ui.activities.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.lifecycleScope
import com.kl3jvi.animity.BuildConfig
import com.kl3jvi.animity.BuildConfig.*
import com.kl3jvi.animity.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), Authentication {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var customTabsIntent: CustomTabsIntent
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            customTabsIntent = CustomTabsIntent.Builder().build()
            customTabsIntent.launchUrl(this@LoginActivity, getAuthorizationUrl())
        }

    }

    override fun onResume() {
        super.onResume()
        onHandleAuthIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        onHandleAuthIntent(intent)
    }

    override fun getAuthorizationUrl(): Uri {
        //https://anilist.co/api/v2/oauth/authorize?client_id={client_id}&redirect_uri={redirect_uri}&response_type=code
        return Uri.Builder().scheme("https")
            .authority("anilist.co")
            .appendPath("api")
            .appendPath("v2")
            .appendPath("oauth")
            .appendPath("authorize")
            .appendQueryParameter("client_id", BuildConfig.ANILIST_ID)
            .appendQueryParameter("redirect_uri", BuildConfig.REDIRECT_URI)
            .appendQueryParameter("response_type", "code")
            .build()
    }

    override fun onHandleAuthIntent(intent: Intent?) {
        if (intent != null && intent.data != null) {
            val uri = intent.data
            if (uri.toString().startsWith(BuildConfig.REDIRECT_URI)) {
                val authorizationToken = uri?.getQueryParameter("code")
                if (!authorizationToken.isNullOrEmpty()) {
                    lifecycleScope.launch {
                        viewModel.getAccessToken(
                            authorizationToken,
                            ANILIST_ID,
                            ANILIST_SECRET,
                            APPLICATION_ID,
                            REDIRECT_URI
                        ).collect { state ->
                            when (state) {
                                is State.Error -> {
                                    showSnack(binding.root, state.message)
                                }
                                is State.Loading -> {
                                    binding.progressBar.show()
                                }
                                is State.Success -> {
                                    onTokenResponse(state.data)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}