package com.kl3jvi.animity.ui.activities.player

import androidx.lifecycle.*
import com.google.android.exoplayer2.ExoPlayer

import com.kl3jvi.animity.data.model.ui_models.Content
import com.kl3jvi.animity.domain.use_cases.GetEpisodeInfoUseCase
import com.kl3jvi.animity.persistence.EpisodeDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
        /* It's getting the episode info from the url and then it's getting the id from the vidCdnUrl. */
        getEpisodeInfoUseCase(url).flatMapLatest { episodeInfo ->
            val id = Regex("id=([^&]+)").find(
                    episodeInfo.data?.vidCdnUrl ?: ""
                )?.value?.removePrefix("id=")
            getEpisodeInfoUseCase.fetchEncryptedAjaxUrl(episodeInfo.data?.vidCdnUrl, id ?: "")
        }.flatMapLatest {
            /* It's fetching the m3u8 file from the url. */
            getEpisodeInfoUseCase.fetchM3U8(it.data)
        }.asLiveData()
    }

    fun insertOrUpdate(content: Content) {
        viewModelScope.launch(ioDispatcher) {
            /* It's checking if the episode is on the database and if the watched duration is greater
            than 0. */
            if (episodeDao.isEpisodeOnDatabase(content.episodeUrl) && content.watchedDuration > 0) {
                /* It's updating the episode in the database. */
                episodeDao.updateEpisode(content)
            } else {
                /* It's inserting the episode into the database. */
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
