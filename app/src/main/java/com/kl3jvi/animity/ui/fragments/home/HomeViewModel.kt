package com.kl3jvi.animity.ui.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.data.model.ui_models.HomeRecycleViewItemData
import com.kl3jvi.animity.domain.use_cases.GetAnimesUseCase
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
    val mutableList = mutableListOf<HomeRecycleViewItemData>()

    private var _homeData = MutableLiveData<Resource<List<HomeRecycleViewItemData>>>()
    var homeData: LiveData<Resource<List<HomeRecycleViewItemData>>> = _homeData


    var recentSubDub: LiveData<Resource<List<AnimeMetaModel>>> = _recentSubDub
    var todaySelection: LiveData<Resource<List<AnimeMetaModel>>> = _todaySelection
    var newSeason: LiveData<Resource<List<AnimeMetaModel>>> = _newSeason
    var movies: LiveData<Resource<List<AnimeMetaModel>>> = _movies

    init {
        getHomePageData()
//        fetchRecentSubOrDub()
////        fetchTodaySelectionAnime()
////        fetchNewSeason()
////        fetchMovies()
////        getHomePageData()
    }

    fun getHomePageData() {
        getAnimesUseCase().onEach {
            _homeData.value = it
        }.launchIn(viewModelScope)
    }


//    private fun fetchRecentSubOrDub() {
//        getAnimesUseCase.fetchRecentSubOrDub()
//            .onEach { res ->
////                _recentSubDub.value = it
//                when (res) {
//                    is Resource.Success -> {
//                        mutableList.add(
//                            HomeRecycleViewItemData(
//                                "Recent Sub",
//                                res.data ?: emptyList()
//                            )
//                        )
//                    }
//                }
//
//                _homeData.postValue(mutableList)
//            }.launchIn(viewModelScope)
//    }

    private fun fetchTodaySelectionAnime() {
        getAnimesUseCase.fetchTodaySelectionAnime().onEach { res ->
//                _recentSubDub.value = it
            when (res) {
                is Resource.Success -> {
                    mutableList.add(
                        HomeRecycleViewItemData(
                            "Recent Sub",
                            res.data ?: emptyList()
                        )
                    )
                }
            }
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
