package com.kl3jvi.animity.ui.activities.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.ExoPlayer
import com.kl3jvi.animity.data.model.ui_models.EpisodeEntity
import com.kl3jvi.animity.domain.repositories.PlayerRepository
import com.kl3jvi.animity.utils.Result
import com.kl3jvi.animity.utils.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
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

    val episodeMediaUrl = episodeUrl.flatMapLatest {
        playerRepository.getMediaUrl(url = it).asResult().map { result ->
            when (result) {
                is Result.Error -> EpisodeUrlUiState.Error
                Result.Loading -> EpisodeUrlUiState.Loading
                is Result.Success -> EpisodeUrlUiState.Success(result.data)
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        EpisodeUrlUiState.Loading
    )

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

sealed interface EpisodeUrlUiState {
    data class Success(val data: List<String>) : EpisodeUrlUiState
    object Loading : EpisodeUrlUiState
    object Error : EpisodeUrlUiState
}
