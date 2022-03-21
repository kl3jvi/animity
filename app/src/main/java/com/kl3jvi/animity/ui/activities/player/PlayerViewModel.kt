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
            getEpisodeInfoUseCase.fetchEncryptedAjaxUrl(episodeInfo.data?.vidCdnUrl)
        }.flatMapLatest {
            getEpisodeInfoUseCase.fetchM3U8(it.data)
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
