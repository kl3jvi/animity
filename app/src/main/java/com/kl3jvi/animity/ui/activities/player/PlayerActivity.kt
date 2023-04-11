package com.kl3jvi.animity.ui.activities.player

import android.app.PictureInPictureParams
import android.os.Build
import android.os.Bundle
import android.util.Rational
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
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.MappingTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.TrackSelectionDialogBuilder
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.util.Util
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.kl3jvi.animity.R
import com.kl3jvi.animity.data.model.ui_models.EpisodeEntity
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import com.kl3jvi.animity.databinding.ActivityPlayerBinding
import com.kl3jvi.animity.settings.Settings
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Constants.Companion.ANIME_TITLE
import com.kl3jvi.animity.utils.Constants.Companion.EPISODE_DETAILS
import com.kl3jvi.animity.utils.Constants.Companion.MAL_ID
import com.kl3jvi.animity.utils.Constants.Companion.REFERER
import com.kl3jvi.animity.utils.Constants.Companion.getSafeString
import com.kl3jvi.animity.utils.Constants.Companion.showSnack
import com.kl3jvi.animity.utils.UiResult
import com.kl3jvi.animity.utils.collect
import com.kl3jvi.animity.utils.logError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.Cache
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import java.io.File
import java.net.InetAddress
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class PlayerActivity : AppCompatActivity() {

    @Inject
    lateinit var settings: Settings

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityPlayerBinding.inflate(layoutInflater)
    }
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var cacheFactory: CacheDataSource.Factory
    private var player: ExoPlayer? = null
    private val viewModel: PlayerViewModel by viewModels()
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
    private lateinit var episodeNumberLocal: String
    lateinit var episodeUrlLocal: String
    lateinit var episodeEntity: EpisodeEntity
    var aniListId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        firebaseAnalytics = Firebase.analytics
        if (intent.hasExtra(EPISODE_DETAILS)) {
            val getIntentData = intent.getParcelableExtra<EpisodeModel>(EPISODE_DETAILS)

            aniListId = intent.getIntExtra(MAL_ID, 0)
            animeTitlePassed = intent.getStringExtra(ANIME_TITLE).toString()
            episodeNumberLocal = getIntentData?.episodeNumber.toString()
            episodeUrlLocal = getIntentData?.episodeUrl.toString()

            val title = binding.videoView.findViewById<TextView>(R.id.episodeName)
            val episodeNum = binding.videoView.findViewById<TextView>(R.id.episodeNum)

            title.text = animeTitlePassed
            episodeNum.text = getIntentData?.episodeNumber

            initialisePlayerLayout()
            viewModel.episodeUrl.value = getIntentData?.episodeUrl.toString()
            hideSystemUi()
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        enterPipMode()
    }

    private fun enterPipMode() {
        if (!settings.pipEnabled) return
        try {
            val aspectRatio = Rational(128, 72)
            binding.videoView.useController = true
            binding.videoView.controllerAutoShow = false
            binding.videoView.player?.play()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                enterPictureInPictureMode(
                    PictureInPictureParams
                        .Builder()
                        .setAspectRatio(aspectRatio)
                        .build()
                )
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                enterPictureInPictureMode()
            }
        } catch (e: Exception) {
            logError(e)
        }
    }

    private fun insertEpisodeToDatabase(episodeEntity: EpisodeEntity) {
        viewModel.upsertEpisode(episodeEntity = episodeEntity)
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
            player?.pause()
            onIsPlayingChanged(isPlaying = false)
            releasePlayer()
        }
    }

    @ExperimentalCoroutinesApi
    private fun initializePlayer() {
        collect(viewModel.episodeMediaUrl) { res ->
            when (res) {
                is UiResult.Error, UiResult.Loading -> {
                    binding.loadingOverlay.visibility = View.VISIBLE
                }

                is UiResult.Success -> {
                    val videoM3U8Url = getSafeString(res.data.last())
                    try {
                        setupPlayer(videoM3U8Url)
                        viewModel.getPlaybackPosition(episodeUrlLocal)
                        collect(viewModel.playBackPosition) {
                            player?.seekTo(it)
                        }
                        handlePlayerListener()
                        handleSkipIntro()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        showSnack(binding.root, e.localizedMessage)
                    }
                    binding.loadingOverlay.visibility = View.GONE
                }
            }
        }
    }

    private fun setupPlayer(videoM3U8Url: String) {
        trackSelector = DefaultTrackSelector(this).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
            .build()

        player = ExoPlayer.Builder(this)
//            .setMediaSourceFactoryIfM3u8(videoM3U8Url)
            .setAudioAttributes(audioAttributes, true)
            .setTrackSelector(trackSelector!!)
            .setSeekBackIncrementMs(settings.seekBackwardTime)
            .setSeekForwardIncrementMs(settings.seekForwardTime)
            .build().apply {
                binding.videoView.player = this
                val mdItem = MediaItem.fromUri(videoM3U8Url)
                val videoSource = buildMediaSource(mdItem, videoM3U8Url)
                setMediaSource(videoSource)
                playWhenReady = true
                prepare()
            }
    }

//    private fun ExoPlayer.Builder.setMediaSourceFactoryIfM3u8(videoM3U8Url: String) = apply {
//        if (videoM3U8Url.contains("m3u8"))
////            setMediaSourceFactory(DefaultMediaSourceFactory(cacheFactory))
//    }

    private fun handlePlayerListener() {
        player?.addListener(object : Player.Listener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == ExoPlayer.STATE_READY) {
                    val realDurationMillis = player?.duration ?: 0
                    episodeEntity = EpisodeEntity().apply {
                        malId = aniListId
                        episodeUrl = episodeUrlLocal
                        watchedDuration = 0
                        duration = realDurationMillis
                    }
                }
            }
        })
    }

    private fun handleSkipIntro() {
        binding.videoView.findViewById<LinearLayout>(R.id.skipLayout)?.let { skipIntro ->
            collect(viewModel.progress(player)) { currentProgress ->
                currentTime = currentProgress
                if (currentTime < 300000) {
                    skipIntro.setOnClickListener {
                        player?.seekTo(currentTime + settings.skipIntroDelay)
                        skipIntro.visibility = View.GONE
                    }
                } else {
                    skipIntro.visibility = View.GONE
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (::episodeEntity.isInitialized) {
            insertEpisodeToDatabase(episodeEntity.copy(watchedDuration = player!!.currentPosition))
        }
    }

    private fun initialisePlayerLayout() {
        val backButton = binding.videoView.findViewById<ImageView>(R.id.back)
        backButton.setOnClickListener {
            if (::episodeEntity.isInitialized) {
                insertEpisodeToDatabase(episodeEntity.copy(watchedDuration = player!!.currentPosition))
            }
            finish()
        }

        val qualityButton =
            binding.videoView.findViewById<ImageButton>(R.id.exo_track_selection_view)
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

        val fullView = binding.videoView.findViewById<ImageView>(R.id.exo_full_Screen)
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
                player!!,
                0
            ).setTheme(R.style.MaterialThemeDialog)
                .setTrackNameProvider { f: Format ->
                    "${f.height}p"
                }.build().show()
        } catch (ignored: NullPointerException) {
            ignored.printStackTrace()
        }
    }

//    private fun buildMediaSource(mediaItem: MediaItem): HlsMediaSource {
//        val dataSourceFactory: DataSource.Factory = getDataSourceFactory()
//        return HlsMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
//    }

    private fun buildMediaSource(mediaItem: MediaItem, url: String): MediaSource {

        return if (url.contains("m3u8")) {
            val appCache = Cache(File("cacheDir", "okhttpcache"), 10 * 1024 * 1024)
            val bootstrapClient = OkHttpClient.Builder().cache(appCache).build()

            val dns = DnsOverHttps.Builder().client(bootstrapClient)
                .url("https://security.cloudflare-dns.com/dns-query".toHttpUrl())
                .bootstrapDnsHosts(InetAddress.getByName("1.1.1.1"))
                .build()

            val client = bootstrapClient.newBuilder().dns(dns).build()
            val dataSource = {
                val dataSource = OkHttpDataSource.Factory(client)
                    .setUserAgent(Constants.USER_AGENT)
                    .setDefaultRequestProperties(hashMapOf("Referer" to REFERER))
                dataSource.createDataSource()
            }
            val simpleCache = VideoCacheManager.getInstance(this)
            cacheFactory = CacheDataSource.Factory().apply {
                setCache(simpleCache)
                setUpstreamDataSourceFactory(dataSource)
            }

            HlsMediaSource.Factory(dataSource)
                .setAllowChunklessPreparation(true)
                .createMediaSource(mediaItem)
        } else {
            /* Creating a data source factory. */
            val dataSource = {
                val dataSource: DataSource.Factory = DefaultHttpDataSource.Factory()
                    .setUserAgent(Constants.USER_AGENT)
                    .setDefaultRequestProperties(hashMapOf("Referer" to REFERER))
                dataSource.createDataSource()
            }
            ProgressiveMediaSource.Factory(dataSource)
                .createMediaSource(mediaItem)
        }
    }


    /**
     * It releases the player.
     */
    private fun releasePlayer() {
        player?.run {
            playbackPosition = this.currentPosition
//            currentWindow = this.currentWindowIndex
            playWhenReady = this.playWhenReady
            release()
        }
        player = null
    }

    /**
     * > When the video is playing, keep the screen on
     *
     * @param isPlaying Boolean
     */
    private fun onIsPlayingChanged(isPlaying: Boolean) {
        binding.videoView.keepScreenOn = isPlaying
    }

    /**
     * It shows a dialog for speed selection.
     */
    private fun showDialogForSpeedSelection() {
        val builder = AlertDialog.Builder(this, R.style.MaterialThemeDialog)
        builder.apply {
            setTitle("Set your playback speed")
            setSingleChoiceItems(shownSpeed, checkedItem) { _, which ->
                setSpeed(which)
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

    /**
     * > The function sets the speed to the speed passed in and sets the checked item to the speed
     *
     * @param speed The speed to set the playback to.
     */
    private fun setSpeed(speed: Int) {
        selectedSpeed = speed
        checkedItem = speed
    }

    /**
     * It sets the playback speed of the video player to the speed passed in as a parameter
     *
     * @param speed The playback speed.
     */
    private fun setPlaybackSpeed(speed: Float) {
        val params = PlaybackParameters(speed)
        player?.playbackParameters = params
    }

    /**
     * It toggles the full screen mode of the video player.
     */
    private fun toggleFullView() {
        val resizeMode = if (isFullScreen) {
            AspectRatioFrameLayout.RESIZE_MODE_FIT
        } else {
            AspectRatioFrameLayout.RESIZE_MODE_FILL
        }
        binding.videoView.resizeMode = resizeMode

        val videoScalingMode = if (isFullScreen) {
            C.VIDEO_SCALING_MODE_SCALE_TO_FIT
        } else {
            C.VIDEO_SCALING_MODE_DEFAULT
        }
        player?.videoScalingMode = videoScalingMode

        isFullScreen = !isFullScreen

        val fullScreenIcon = if (isFullScreen) {
            R.drawable.exo_controls_fullscreen_exit
        } else {
            R.drawable.exo_controls_fullscreen_enter
        }
        val fullScreenDrawable = ContextCompat.getDrawable(this, fullScreenIcon)
        binding.videoView.findViewById<ImageView>(R.id.exo_full_Screen)
            ?.setImageDrawable(fullScreenDrawable)
    }

    /**
     * Hide the system UI and make it re-appear when the user swipes down from the top of the screen.
     */
    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.videoView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    companion object {
        val defaultHeaders = mapOf(
            "User-Agent" to
                    "Mozilla/5.0 (Linux; Android %s; %s) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Mobile Safari/537.36"
                        .format(Build.VERSION.RELEASE, Build.MODEL)
        )
    }
}
