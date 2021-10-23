package com.kl3jvi.animity.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kl3jvi.animity.domain.GetAnimeDetailsUseCase
import com.kl3jvi.animity.model.database.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getAnimeDetailsUseCase: GetAnimeDetailsUseCase,
    private val animeRepository: AnimeRepository
) : ViewModel() {

    private val _url = MutableLiveData<String>()
    private val episodeData = MutableLiveData<List<String>>()

    val animeInfo = Transformations.switchMap(_url) { string ->
        getAnimeDetailsUseCase.fetchAnimeInfo(string).asLiveData()
    }

    val episodeList = Transformations.switchMap(episodeData) { list ->
        getAnimeDetailsUseCase.fetchEpisodeList(list[0], list[1], list[2]).asLiveData()
    }

    fun passUrl(url: String) {
        _url.value = url
    }

    fun passEpisodeData(
        id: String,
        endEpisode: String,
        alias: String
    ) {
        val list = listOf(id, endEpisode, alias)
        episodeData.value = list
    }
}

