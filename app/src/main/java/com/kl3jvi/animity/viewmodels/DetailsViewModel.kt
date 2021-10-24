package com.kl3jvi.animity.viewmodels

import androidx.lifecycle.*
import com.kl3jvi.animity.domain.GetAnimeDetailsUseCase
import com.kl3jvi.animity.model.database.AnimeRepository
import com.kl3jvi.animity.model.entities.AnimeMetaModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getAnimeDetailsUseCase: GetAnimeDetailsUseCase,
    private val animeRepository: AnimeRepository
) : ViewModel() {

    private val _url = MutableLiveData<String>()
    private val _animeId = MutableLiveData<Int>()
    private val _episodeData = MutableLiveData<List<String>>()
    private val _isAnimeOnDatabase = MutableLiveData<Boolean>()


    val isAnimeOnDatabase: LiveData<Boolean> = _isAnimeOnDatabase
    val animeInfo = Transformations.switchMap(_url) { string ->
        getAnimeDetailsUseCase.fetchAnimeInfo(string).asLiveData()
    }
    val episodeList = Transformations.switchMap(_episodeData) { list ->
        getAnimeDetailsUseCase.fetchEpisodeList(list[0], list[1], list[2]).asLiveData()
    }
    val isOnDatabase = Transformations.switchMap(_animeId){ id->
        getAnimeDetailsUseCase.checkIfExists(id).asLiveData()
    }


    fun passUrl(url: String) {
        _url.value = url
    }

    fun passId(id:Int){
        _animeId.value = id
    }

    fun passEpisodeData(id: String, endEpisode: String, alias: String) {
        val list = listOf(id, endEpisode, alias)
        _episodeData.value = list
    }


    fun insert(anime: AnimeMetaModel) = viewModelScope.launch {
        animeRepository.insertFavoriteAnime(anime)
    }


    fun delete(anime: AnimeMetaModel) = viewModelScope.launch {
        animeRepository.deleteAnime(anime)
    }



}

