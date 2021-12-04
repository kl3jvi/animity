package com.kl3jvi.animity.ui.activities.player

import androidx.lifecycle.*
import com.google.android.exoplayer2.ExoPlayer
import com.kl3jvi.animity.domain.GetEpisodeInfoUseCase
import com.kl3jvi.animity.model.Content
import com.kl3jvi.animity.persistence.EpisodeDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val getEpisodeInfoUseCase: GetEpisodeInfoUseCase,
    private val episodeDao: EpisodeDao
) : ViewModel() {

    private var _vidUrl = MutableLiveData<String>()
    fun updateEpisodeUrl(vidUrl: String) {
        _vidUrl.value = vidUrl
    }

    @ExperimentalCoroutinesApi
    val videoUrlLiveData = Transformations.switchMap(_vidUrl) { url ->
        getEpisodeInfoUseCase.fetchEpisodeMediaUrl(url).flatMapLatest { episodeInfo ->
            getEpisodeInfoUseCase.fetchM3U8(episodeInfo.data?.vidCdnUrl)
        }.asLiveData()
    }

    fun audioProgress(exoPlayer: ExoPlayer?) = flow {
        exoPlayer?.currentPosition?.let {
            while (it < 200000) {
                emit(exoPlayer.currentPosition)
                delay(1000)
            }
        }
    }.flowOn(Dispatchers.Main).asLiveData(Dispatchers.Default + viewModelScope.coroutineContext)


    fun insertOrUpdate(content: Content) {
        viewModelScope.launch {
            if (episodeDao.isEpisodeOnDatabase(content.episodeUrl) && content.watchedDuration > 0) {
                episodeDao.updateEpisode(content)
            } else {
                episodeDao.insertEpisode(content)
            }
        }
    }

}
