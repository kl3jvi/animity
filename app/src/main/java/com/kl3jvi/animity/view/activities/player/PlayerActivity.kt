package com.kl3jvi.animity.view.activities.player

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.accessibility.AccessibilityRecordCompat.setSource
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsDataSourceFactory
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.ActivityPlayerBinding
import com.kl3jvi.animity.model.network.ApiHelper
import com.kl3jvi.animity.model.network.RetrofitBuilder
import com.kl3jvi.animity.utils.Constants
import com.potyvideo.library.globalInterfaces.AndExoPlayerListener
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy

private lateinit var viewModel: PlayerViewModel
private var episodeNumber: String? = ""

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this,
            PlayerViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(PlayerViewModel::class.java)
        getExtra(intent)


        val DEFAULT_COOKIE_MANAGER = CookieManager()
            DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ALL)


        if (CookieHandler.getDefault() !== DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER)
        }

    }

    private fun getExtra(intent: Intent?) {
        val url = intent?.extras?.getString("episodeUrl")
        episodeNumber = intent?.extras?.getString("episodeNumber")

        url?.let { episodeUrl ->

            viewModel.fetchEpisodeMediaUrl(episodeUrl).observe(this, {
                it.data?.let { data ->
                    viewModel.fetchM3U8(data).observe(this, { videoUrl ->
                        binding.andExoPlayerView.apply {
                            setSource(videoUrl.data.toString(),
                                hashMapOf(Pair("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.82 Mobile Safari/537.36",Constants.REFERER))
                            )
                        }
                    })
                }
            })
        }
    }




}