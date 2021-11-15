package com.kl3jvi.animity.ui.activities.player

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.MappingTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.TrackSelectionDialogBuilder
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.util.Util
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.ActivityPlayerBinding
import com.kl3jvi.animity.model.entities.EpisodeModel
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Constants.Companion.getDataSourceFactory
import com.kl3jvi.animity.utils.Resource
import com.kl3jvi.animity.viewmodels.PlayerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class PlayerActivity : AppCompatActivity() {

    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityPlayerBinding.inflate(layoutInflater)
    }

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var player: ExoPlayer? = null
    private val viewModel: PlayerViewModel by viewModels()
    private var playWhenReady = true
    private var playbackPosition = 0L
    private val speeds = arrayOf(0.25f, 0.5f, 1f, 1.25f, 1.5f, 2f)
    private val shownSpeed = arrayOf("0.25x", "0.50x", "1x", "1.25x", "1.50x", "2x")
    private var checkedItem = 2
    private var selectedSpeed = 2
    private var isFullScreen = false
    private var mappedTrackInfo: MappingTrackSelector.MappedTrackInfo? = null
    private var trackSelector: DefaultTrackSelector? = null
    private var currentTime = 0L
    private lateinit var animeTitlePassed: String
    lateinit var episodeNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        firebaseAnalytics = Firebase.analytics

        if (intent.hasExtra(Constants.EPISODE_DETAILS)) {
            val getIntentData = intent.getParcelableExtra<EpisodeModel>(Constants.EPISODE_DETAILS)
            animeTitlePassed = intent.getStringExtra(Constants.ANIME_TITLE).toString()
            episodeNumber = getIntentData?.episodeNumber.toString()
            val title = viewBinding.videoView.findViewById<TextView>(R.id.episodeName)
            val episodeNum = viewBinding.videoView.findViewById<TextView>(R.id.episodeNum)

            title.text = animeTitlePassed
            episodeNum.text = getIntentData?.episodeNumber

            initialisePlayerLayout()
            viewModel.updateEpisodeUrl(getIntentData?.episodeurl.toString())
        }
    }

    public override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23 && player == null) {
            initializePlayer()
            onIsPlayingChanged(isPlaying = true)
        }
    }

    public override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <= 23 && player == null) {
            initializePlayer()
            onIsPlayingChanged(isPlaying = true)
        }
        player?.playWhenReady = true
    }

    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23 && player == null) {
            releasePlayer()
            onIsPlayingChanged(isPlaying = false)
        }
        player?.playWhenReady = false
    }

    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23 && player == null) {
            if (player != null) {
                player?.pause()
                onIsPlayingChanged(isPlaying = false)
            }
            releasePlayer()
        }
    }

    @ExperimentalCoroutinesApi
    private fun initializePlayer() {
        viewModel.videoUrlLiveData.observe(this, { res ->
            when (res) {
                is Resource.Success -> {
                    val videoM3U8Url = res.data.toString()
                    try {
                        trackSelector = DefaultTrackSelector(this).apply {
                            setParameters(buildUponParameters().setMaxVideoSizeSd())
                        }
                        val audioAttributes: AudioAttributes = AudioAttributes.Builder()
                            .setUsage(C.USAGE_MEDIA)
                            .setContentType(C.CONTENT_TYPE_MOVIE)
                            .build()

                        player = ExoPlayer.Builder(this)
                            .setAudioAttributes(audioAttributes, true)
                            .setTrackSelector(trackSelector!!)
                            .build()
                            .also { exoPlayer ->
                                viewBinding.videoView.player = exoPlayer
                                val mdItem = MediaItem.fromUri(videoM3U8Url)
                                val videoSource: MediaSource =
                                    buildMediaSource(mdItem)
                                exoPlayer.setMediaSource(videoSource)
                                exoPlayer.playWhenReady = playWhenReady
                                exoPlayer.seekTo(playbackPosition)
                                exoPlayer.prepare()
                            }

                        val skipIntro =
                            viewBinding.videoView.findViewById<LinearLayout>(R.id.skipLayout)
                        viewModel.audioProgress(player).observe(this, { currentProgress ->
                            currentProgress?.let {
                                currentTime = it
                                if (currentTime < 300000) {
                                    skipIntro.setOnClickListener {
                                        player?.seekTo(currentTime + Constants.INTRO_SKIP_TIME)
                                        skipIntro.visibility = View.GONE
                                    }
                                } else {
                                    skipIntro.visibility = View.GONE
                                }
                            }
                        })
                    } catch (e: ExoPlaybackException) {
                        showSnack(e.localizedMessage)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    viewBinding.loadingOverlay.visibility = View.GONE
                }
                is Resource.Loading -> {
                    viewBinding.loadingOverlay.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    showSnack(res.message)
                }
            }
        })
    }

    private fun initialisePlayerLayout() {

        val backButton = viewBinding.videoView.findViewById<ImageView>(R.id.back)
        backButton.setOnClickListener {
            finish()
        }

        val qualityButton =
            viewBinding.videoView.findViewById<ImageButton>(R.id.exo_track_selection_view)
        qualityButton.setOnClickListener {
            val popMenu = PopupMenu(this, qualityButton)
            popMenu.menuInflater.inflate(R.menu.exo_player_menu, popMenu.menu)
            popMenu.setOnMenuItemClickListener {
                if (it.itemId == R.id.quality) {
                    showQualityDialog()
                } else if (it.itemId == R.id.playback_speed) {
                    showDialogForSpeedSelection()
                }
                true
            }
            popMenu.show()
        }

        val fullView = viewBinding.videoView.findViewById<ImageView>(R.id.exo_full_Screen)
        fullView.setOnClickListener {
            toggleFullView()
        }
    }

    private fun showQualityDialog() {
        mappedTrackInfo = trackSelector?.currentMappedTrackInfo
        try {
            TrackSelectionDialogBuilder(
                this,
                getString(R.string.video_quality),
                trackSelector!!,
                0
            ).setTheme(R.style.MaterialThemeDialog)
                .setTrackNameProvider { f: Format ->
                    "${f.height}p"
                }
                .build()
                .show()
        } catch (ignored: NullPointerException) {
            ignored.printStackTrace()
        }
    }

    private fun buildMediaSource(mediaItem: MediaItem): HlsMediaSource {
        val dataSourceFactory: DataSource.Factory = getDataSourceFactory()
        return HlsMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
    }

    private fun releasePlayer() {
        player?.run {
            playbackPosition = this.currentPosition
//            currentWindow = this.currentWindowIndex
            playWhenReady = this.playWhenReady
            release()
        }
        player = null
    }

    private fun onIsPlayingChanged(isPlaying: Boolean) {
        viewBinding.videoView.keepScreenOn = isPlaying
    }

    private fun showSnack(message: String?) {
        val snack =
            Snackbar.make(viewBinding.root, message ?: "Error Occurred", Snackbar.LENGTH_LONG)
        if (!snack.isShown) {
            snack.show()
        }
    }

    private fun showDialogForSpeedSelection() {
        val builder = AlertDialog.Builder(this, R.style.MaterialThemeDialog)
        builder.apply {
            setTitle("Set your playback speed")
            setSingleChoiceItems(shownSpeed, checkedItem) { _, which ->
                when (which) {
                    0 -> setSpeed(0)
                    1 -> setSpeed(1)
                    2 -> setSpeed(2)
                    3 -> setSpeed(3)
                    4 -> setSpeed(4)
                    5 -> setSpeed(5)
                }
            }
            setPositiveButton("OK") { dialog, _ ->
                setPlaybackSpeed(speeds[selectedSpeed])
                dialog.dismiss()
            }
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun setSpeed(speed: Int) {
        selectedSpeed = speed
        checkedItem = speed
    }

    private fun setPlaybackSpeed(speed: Float) {
        val params = PlaybackParameters(speed)
        player?.playbackParameters = params
    }

    private fun toggleFullView() {
        if (isFullScreen) {
            viewBinding.videoView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            player?.videoScalingMode = C.VIDEO_SCALING_MODE_DEFAULT
            isFullScreen = false
            this.let {
                viewBinding.videoView.findViewById<ImageView>(R.id.exo_full_Screen)
                    .setImageDrawable(
                        ContextCompat.getDrawable(
                            it,
                            R.drawable.exo_controls_fullscreen_enter
                        )
                    )
            }
        } else {
            viewBinding.videoView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
            player?.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
            isFullScreen = true
            this.let {
                viewBinding.videoView.findViewById<ImageView>(R.id.exo_full_Screen)
                    .setImageDrawable(
                        ContextCompat.getDrawable(
                            it,
                            R.drawable.exo_controls_fullscreen_exit
                        )
                    )
            }
        }
    }
}
