package com.kl3jvi.animity.ui.activities.player

import androidx.lifecycle.*
import com.google.android.exoplayer2.ExoPlayer
import com.kl3jvi.animity.data.model.ui_models.Content
import com.kl3jvi.animity.domain.use_cases.GetEpisodeInfoUseCase
import com.kl3jvi.animity.persistence.EpisodeDao
import com.kl3jvi.animity.utils.Result
import com.kl3jvi.animity.utils.logError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class PlayerViewModel @Inject constructor(
    private val getEpisodeInfoUseCase: GetEpisodeInfoUseCase,
    private val episodeDao: EpisodeDao,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var _episodeUrl = MutableLiveData<String>()
    private var _playBackPosition = MutableLiveData<Long>()

    fun updateEpisodeUrl(vidUrl: String) {
        _episodeUrl.value = vidUrl
    }

    /**
     * We create a flow that emits the current position of the ExoPlayer every second, and then we
     * convert it to a LiveData object
     *
     * @param exoPlayer ExoPlayer? - The ExoPlayer instance that you want to observe.
     */
    fun audioProgress(exoPlayer: ExoPlayer?) = flow {
        exoPlayer?.currentPosition?.let {
            while (it < 200000) {
                emit(exoPlayer.currentPosition)
                delay(1000)
            }
        }
    }.flowOn(Dispatchers.Main).asLiveData(Dispatchers.IO + viewModelScope.coroutineContext)

    val videoUrlLiveData = Transformations.switchMap(_episodeUrl) { url ->
        getEpisodeInfoUseCase(url).flatMapLatest { episodeInfo ->
            when (episodeInfo) {
                is Result.Error -> {
                    logError(episodeInfo.exception)
                    emptyFlow()
                }
                is Result.Loading -> {
                    emptyFlow()
                }
                is Result.Success -> {
                    val id = Regex("id=([^&]+)").find(
                        episodeInfo.data.vidCdnUrl ?: ""
                    )?.value?.removePrefix("id=")
                    getEpisodeInfoUseCase.fetchEncryptedAjaxUrl(
                        episodeInfo.data.vidCdnUrl,
                        id ?: ""
                    )
                }
            }
        }.flatMapLatest {
            when (it) {
                is Result.Error -> emptyFlow()
                Result.Loading -> emptyFlow()
                is Result.Success -> getEpisodeInfoUseCase.fetchM3U8(it.data)
            }
        }.asLiveData()
    }

    fun insertOrUpdate(content: Content) {
        viewModelScope.launch(ioDispatcher) {
            if (episodeDao.isEpisodeOnDatabase(content.episodeUrl) && content.watchedDuration > 0) {
                episodeDao.updateEpisode(content)
            } else {
                episodeDao.insertEpisode(content)
            }
        }
    }

    /**
     * It's getting the playback position of the episode from the database and posting it to the
     * _playBackPosition
     *
     * @param episodeUrl The url of the episode that is being played.
     * @return The _playBackPosition is being returned.
     */
    fun getPlaybackPosition(episodeUrl: String): LiveData<Long> {
        viewModelScope.launch(ioDispatcher) {
            if (episodeDao.isEpisodeOnDatabase(episodeUrl)) {
                episodeDao.getEpisodeContent(episodeUrl).collectLatest {
                    _playBackPosition.postValue(it.watchedDuration)
                }
            }
        }
        return _playBackPosition
    }
}
