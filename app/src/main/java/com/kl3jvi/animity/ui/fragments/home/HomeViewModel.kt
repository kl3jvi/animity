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

    private var _homeData = MutableLiveData<Resource<List<HomeRecycleViewItemData>>>()
    var homeData: LiveData<Resource<List<HomeRecycleViewItemData>>> = _homeData

    init {
        getHomePageData()
    }

    private fun getHomePageData() {
        getAnimesUseCase().onEach {
            _homeData.value = it
        }.launchIn(viewModelScope)
    }

}
