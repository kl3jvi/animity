package com.kl3jvi.animity.ui.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.data.model.ui_models.HomeData
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.domain.use_cases.GetAnimesUseCase
import com.kl3jvi.animity.utils.NetworkResource
import com.kl3jvi.animity.utils.Resource
import com.kl3jvi.animity.utils.logError
import com.kl3jvi.animity.utils.logMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAnimesUseCase: GetAnimesUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var _homeData = MutableLiveData<Resource<MutableList<List<AnimeMetaModel>>>>()
    var homeData: LiveData<Resource<MutableList<List<AnimeMetaModel>>>> = _homeData

    init {
//        getHomePageData()
        getHomeNewData()
    }

//    private fun getHomePageData() {
//        getAnimesUseCase().flowOn(ioDispatcher).catch { e ->
//            logError(e)
//        }.catch { e -> logError(e) }
//            .onEach {
//                when (it) {
//                    is Resource.Error -> {
//                        logMessage(it.message)
//                    }
//                    is Resource.Loading -> {
//
//                    }
//                    is Resource.Success -> {
//                        _homeData.value = it
//                    }
//                }
//            }.launchIn(viewModelScope)
//    }

    private val _homeNewData = MutableStateFlow(HomeData())
    val homeNewData = _homeNewData.asStateFlow()

    private fun getHomeNewData() {
        viewModelScope.launch(Dispatchers.IO) {
            getAnimesUseCase.test().collect {
                when (it) {
                    is NetworkResource.Failed -> {
                        logMessage(it.message)
                    }
                    is NetworkResource.Success -> {
                        _homeNewData.value = it.data
                    }
                }

            }
        }
    }
}
