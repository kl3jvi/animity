package com.kl3jvi.animity.view.activities.player

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import com.google.android.material.snackbar.Snackbar
import com.kl3jvi.animity.databinding.ActivityPlayerBinding
import com.kl3jvi.animity.model.entities.EpisodeModel
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Constants.Companion.USER_AGENT
import com.kl3jvi.animity.utils.Resource
import com.kl3jvi.animity.viewmodels.PlayerViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PlayerActivity : AppCompatActivity() {

    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityPlayerBinding.inflate(layoutInflater)
    }
    private var vidUrl: String = "null"
    private var player: SimpleExoPlayer? = null
    private val viewModel: PlayerViewModel by viewModels()
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        if (intent.hasExtra(Constants.EPISODE_DETAILS)) {
            val getIntentData = intent.getParcelableExtra<EpisodeModel>(Constants.EPISODE_DETAILS)
            viewModel.updateEpisodeUrl(getIntentData?.episodeurl.toString())
            observeData()
        }
    }


    private fun observeData() {
        viewModel.videoUrlLiveData.observe(this, { status ->
            when (status) {
                is Resource.Success -> {
                    vidUrl = status.data?.vidCdnUrl.toString()
                    viewModel.updateUrlForFetch(vidUrl)
                }
                else -> vidUrl = ""
            }
        })
    }

    public override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23 && player == null) {
            initializePlayer()
        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT <= 23 && player == null) {
            initializePlayer()
        }
        player?.playWhenReady = true
    }

    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23 && player == null) {
            releasePlayer()
        }
        player?.playWhenReady = false
    }

    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23 && player == null) {
            if (player != null) {
                player?.pause()
            }
            releasePlayer()
        }
    }

    private fun initializePlayer() {
        viewModel.fetchM3U8.observe(this, { res ->
            when (res) {
                is Resource.Success -> {
                    val videoM3U8Url = res.data.toString()
                    Log.e("Video URL", videoM3U8Url)
                    try {

                        val trackSelector = DefaultTrackSelector(this).apply {
                            setParameters(buildUponParameters().setMaxVideoSizeSd())
                        }
                        val videoSource: MediaSource = buildMediaSource(Uri.parse(videoM3U8Url))
                        player = SimpleExoPlayer.Builder(this)
                            .setTrackSelector(trackSelector)
                            .build()
                            .also { exoPlayer ->
                                viewBinding.videoView.player = exoPlayer
                                val mediaItem = MediaItem.Builder()
                                    .setUri(videoM3U8Url)
                                    .setMimeType(MimeTypes.APPLICATION_M3U8)
                                    .build()

                                exoPlayer.setMediaItem(mediaItem)
                                exoPlayer.setMediaSource(videoSource)
                                exoPlayer.playWhenReady = playWhenReady
                                exoPlayer.seekTo(currentWindow, playbackPosition)
                                exoPlayer.prepare()
                            }
                    } catch (e: ExoPlaybackException) {
                        showSnack(e.localizedMessage)
                    }
                    viewBinding.progress.visibility = View.GONE
                    hideSystemUi()
                }
                is Resource.Loading -> {
                    viewBinding.progress.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    hideSystemUi()
                    showSnack(res.message)
                }
            }
        })
    }

    private fun getDataSourceFactory(currentUrl: String?): DefaultHttpDataSource.Factory {
        return DefaultHttpDataSource.Factory().apply {
            setUserAgent(USER_AGENT)
            if (currentUrl != null) {
                val headers = mapOf(
                    "referer" to Constants.REFERER,
                    "accept" to "*/*",
                    "sec-ch-ua" to "\"Chromium\";v=\"91\", \" Not;A Brand\";v=\"99\"",
                    "sec-ch-ua-mobile" to "?0",
                    "sec-fetch-user" to "?1",
                    "sec-fetch-mode" to "navigate",
                    "sec-fetch-dest" to "video"
                ) + Constants.getHeader() // Adds the headers from the provider, e.g Authorization
                setDefaultRequestProperties(headers)
            }
        }
    }

    private fun buildMediaSource(uri: Uri): HlsMediaSource {
        val dataSourceFactory: DataSource.Factory = getDataSourceFactory(uri.toString())
        return HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
    }

    private fun releasePlayer() {
        player?.run {
            playbackPosition = this.currentPosition
            currentWindow = this.currentWindowIndex
            playWhenReady = this.playWhenReady
            release()
        }
        player = null
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        viewBinding.videoView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    private fun showSnack(message: String?) {
        val snack =
            Snackbar.make(viewBinding.root, message ?: "Error Occurred", Snackbar.LENGTH_LONG)
        if (!snack.isShown) {
            snack.show()
        }
    }

}





