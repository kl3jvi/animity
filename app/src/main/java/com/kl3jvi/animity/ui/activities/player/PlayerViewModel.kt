package com.kl3jvi.animity.ui.activities.player

import androidx.lifecycle.*
import com.google.android.exoplayer2.ExoPlayer
import com.kl3jvi.animity.domain.GetEpisodeInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val getEpisodeInfoUseCase: GetEpisodeInfoUseCase,
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
            while (it < 300000) {
                emit(exoPlayer.currentPosition)
                delay(1000)
            }
        }
    }.flowOn(Dispatchers.Main).asLiveData(Dispatchers.Default + viewModelScope.coroutineContext)
}
