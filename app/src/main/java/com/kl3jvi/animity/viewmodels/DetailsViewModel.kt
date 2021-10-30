package com.kl3jvi.animity.viewmodels

import androidx.lifecycle.*
import com.kl3jvi.animity.domain.GetAnimeDetailsUseCase
import com.kl3jvi.animity.model.database.AnimeRepository
import com.kl3jvi.animity.model.entities.AnimeMetaModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import okhttp3.internal.assertThreadDoesntHoldLock
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getAnimeDetailsUseCase: GetAnimeDetailsUseCase,
    private val animeRepository: AnimeRepository
) : ViewModel() {

    private val _url = MutableLiveData<String>()
    private val _animeId = MutableLiveData<Int>()

    val animeInfo = Transformations.switchMap(_url) { string ->
        getAnimeDetailsUseCase.fetchAnimeInfo(string).asLiveData()
    }

    @ExperimentalCoroutinesApi
    val episodeList = Transformations.switchMap(_url) { list ->
        getAnimeDetailsUseCase.fetchAnimeInfo(list).flatMapLatest { info ->
            getAnimeDetailsUseCase.fetchEpisodeList(
                info.data?.id,
                info.data?.endEpisode,
                info.data?.alias
            )
        }.asLiveData()
    }

    val isOnDatabase = Transformations.switchMap(_animeId) { id ->
        getAnimeDetailsUseCase.checkIfExists(id).asLiveData()
    }

    fun passUrl(url: String) {
        _url.value = url
    }

    fun passId(id: Int) {
        _animeId.value = id
    }

    fun insert(anime: AnimeMetaModel) = viewModelScope.launch {
        animeRepository.insertFavoriteAnime(anime)
    }

    fun delete(anime: AnimeMetaModel) = viewModelScope.launch {
        animeRepository.deleteAnime(anime)
    }
}

