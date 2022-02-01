package com.kl3jvi.animity.ui.fragments.home

import androidx.lifecycle.*
import com.kl3jvi.animity.domain.use_cases.GetAnimesUseCase
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAnimesUseCase: GetAnimesUseCase
) : ViewModel() {

    private var _recentSubDub = MutableLiveData<Resource<List<AnimeMetaModel>>>()
    private var _todaySelection = MutableLiveData<Resource<List<AnimeMetaModel>>>()
    private var _newSeason = MutableLiveData<Resource<List<AnimeMetaModel>>>()
    private var _movies = MutableLiveData<Resource<List<AnimeMetaModel>>>()

    var recentSubDub: LiveData<Resource<List<AnimeMetaModel>>> = _recentSubDub
    var todaySelection: LiveData<Resource<List<AnimeMetaModel>>> = _todaySelection
    var newSeason: LiveData<Resource<List<AnimeMetaModel>>> = _newSeason
    var movies: LiveData<Resource<List<AnimeMetaModel>>> = _movies

    init {
        fetchRecentSubOrDub()
        fetchTodaySelectionAnime()
        fetchNewSeason()
        fetchMovies()
    }

    private fun fetchRecentSubOrDub() {
        getAnimesUseCase.fetchRecentSubOrDub().onEach {
            _recentSubDub.value = it
        }.launchIn(viewModelScope)
    }

    private fun fetchTodaySelectionAnime() {
        getAnimesUseCase.fetchTodaySelectionAnime().onEach {
            _todaySelection.value = it
        }.launchIn(viewModelScope)
    }

    private fun fetchNewSeason() {
        getAnimesUseCase.fetchNewSeason().onEach {
            _newSeason.value = it
        }.launchIn(viewModelScope)
    }

    private fun fetchMovies() {
        getAnimesUseCase.fetchMovies().onEach {
            _movies.value = it
        }.launchIn(viewModelScope)
    }
}
