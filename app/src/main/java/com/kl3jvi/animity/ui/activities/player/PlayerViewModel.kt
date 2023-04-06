package com.kl3jvi.animity.ui.activities.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.ExoPlayer
import com.kl3jvi.animity.data.model.ui_models.EpisodeEntity
import com.kl3jvi.animity.domain.repositories.PlayerRepository
import com.kl3jvi.animity.utils.mapToUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class PlayerViewModel @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    var episodeUrl = MutableStateFlow("")

    private var _playBackPosition = MutableStateFlow<Long>(0)
    var playBackPosition = _playBackPosition.asStateFlow()

    /**
     * It creates a flow that emits the current position of the exoPlayer every second.
     *
     * @param exoPlayer ExoPlayer? - The ExoPlayer instance
     */
    fun progress(exoPlayer: ExoPlayer?) = flow {
        exoPlayer?.currentPosition?.let {
            while (it < 200000) {
                emit(exoPlayer.currentPosition)
                delay(1000)
            }
        }
    }.flowOn(Dispatchers.Main)

    val episodeMediaUrl = episodeUrl.value.let {
        playerRepository.getMediaUrl(url = it, extra = listOf("naruto"))
            .mapToUiState(viewModelScope + ioDispatcher)
    }

    fun upsertEpisode(episodeEntity: EpisodeEntity) {
        viewModelScope.launch(ioDispatcher) { playerRepository.upsertEpisode(episodeEntity) }
    }

    /**
     * The function gets the playback position of the episode from the database and sets the value of the MutableLiveData
     * object to the playback position
     *
     * @param episodeUrl The url of the episode that is being played.
     */
    fun getPlaybackPosition(episodeUrl: String) {
        viewModelScope.launch(ioDispatcher) {
            playerRepository.getPlaybackPosition(episodeUrl).collect { content ->
                _playBackPosition.value = content.watchedDuration
            }
        }
    }
}
