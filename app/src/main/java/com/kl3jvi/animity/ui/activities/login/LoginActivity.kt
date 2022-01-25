package com.kl3jvi.animity.ui.activities.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.kl3jvi.animity.BuildConfig.ANILIST_ID
import com.kl3jvi.animity.BuildConfig.REDIRECT_URI
import com.kl3jvi.animity.databinding.ActivityLoginBinding
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.utils.Constants.Companion.GUEST_LOGIN_TYPE
import com.kl3jvi.animity.utils.Constants.Companion.TERMS_AND_PRIVACY_LINK
import com.kl3jvi.animity.utils.launchActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), Authentication {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var customTabsIntent: CustomTabsIntent
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()

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
        //https://anilist.co/api/v2/oauth/authorize?client_id={client_id}&redirect_uri={redirect_uri}&response_type=token
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
//                if (!authorizationToken.isNullOrEmpty()) {
//                    lifecycleScope.launch {
//                        viewModel.getAccessToken(
//                            GRANT_TYPE,
//                            ANILIST_ID,
//                            ANILIST_SECRET,
//                            REDIRECT_URI,
//                            authorizationToken
//                        ).collect { state ->
//
//                        }
//                    }
//                }
            }
        }
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
}