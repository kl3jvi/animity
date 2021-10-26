package com.kl3jvi.animity.view.activities.player

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.SeekParameters
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.kl3jvi.animity.databinding.ActivityPlayerBinding
import com.kl3jvi.animity.model.entities.EpisodeInfo
import com.kl3jvi.animity.model.entities.EpisodeModel
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.viewmodels.PlayerViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class PlayerActivity : AppCompatActivity() {

    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityPlayerBinding.inflate(layoutInflater)
    }
    private val viewModel: PlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        if (intent.hasExtra(Constants.EPISODE_DETAILS)) {
            val epInfo = intent.getParcelableExtra<EpisodeModel>(Constants.EPISODE_DETAILS)
            Timber.e(epInfo?.episodeurl!!)
            Timber.e(epInfo.episodeNumber)
            Timber.e(epInfo.episodeType)
        }


    }

    fun initPlayer() {

        }
}




