package com.kl3jvi.animity.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.google.android.exoplayer2.SimpleExoPlayer
import com.kl3jvi.animity.domain.GetEpisodeInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val getEpisodeInfoUseCase: GetEpisodeInfoUseCase
) : ViewModel() {

    private var _vidUrl = MutableLiveData<String>()
    private var _mediaUrlForFetch = MutableLiveData<String>()
    fun updateEpisodeUrl(vidUrl: String) {
        _vidUrl.value = vidUrl
    }

    fun updateUrlForFetch(url: String) {
        _mediaUrlForFetch.value = url
    }

    val videoUrlLiveData = Transformations.switchMap(_vidUrl) { url ->
        getEpisodeInfoUseCase.fetchEpisodeMediaUrl(url).asLiveData()
    }

    val fetchM3U8 = Transformations.switchMap(_mediaUrlForFetch) { url ->
        getEpisodeInfoUseCase.fetchM3U8(url).asLiveData()
    }

    fun audioProgress(exoPlayer: SimpleExoPlayer?) = flow {
        exoPlayer?.currentPosition?.let {
            while (it < 300000) {
                emit(exoPlayer.currentPosition)
                delay(1000)
            }
        }
    }.flowOn(Dispatchers.Main).asLiveData()
}
