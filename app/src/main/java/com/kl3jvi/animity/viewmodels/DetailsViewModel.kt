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
    private val episodeData = MutableLiveData<List<String>>()

    private val _isAnimeOnDatabase = MutableLiveData<Boolean>()
    val isAnimeOnDatabase: LiveData<Boolean> = _isAnimeOnDatabase

    val animeInfo = Transformations.switchMap(_url) { string ->
        getAnimeDetailsUseCase.fetchAnimeInfo(string).asLiveData()
    }

    val episodeList = Transformations.switchMap(episodeData) { list ->
        getAnimeDetailsUseCase.fetchEpisodeList(list[0], list[1], list[2]).asLiveData()
    }

    fun passUrl(url: String) {
        _url.value = url
    }

    fun passEpisodeData(id: String, endEpisode: String, alias: String) {
        val list = listOf(id, endEpisode, alias)
        episodeData.value = list
    }


    /**
     * Insert Anime to database
     */
    fun insert(anime: AnimeMetaModel) = viewModelScope.launch {
        animeRepository.insertFavoriteAnime(anime)
    }

    /**
     * Update Anime in database
     */
    fun update(anime: AnimeMetaModel) = viewModelScope.launch {
        animeRepository.updateAnime(anime = anime)
    }

    fun delete(anime: AnimeMetaModel) = viewModelScope.launch {
        animeRepository.deleteAnime(anime)
    }


    fun checkDatabase(id: Int) {
        viewModelScope.launch {
            _isAnimeOnDatabase.value = animeRepository.checkIfAnimeIsOnDatabase(id = id)
        }
    }


}

