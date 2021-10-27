package com.kl3jvi.animity.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kl3jvi.animity.domain.GetEpisodeInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
}
