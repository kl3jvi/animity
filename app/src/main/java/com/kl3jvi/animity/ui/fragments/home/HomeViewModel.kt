package com.kl3jvi.animity.ui.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.domain.use_cases.GetAnimesUseCase
import com.kl3jvi.animity.ui.adapters.homeAdapter.HomeRecyclerViewItem
import com.kl3jvi.animity.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAnimesUseCase: GetAnimesUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var _homeData = MutableLiveData<Resource<List<HomeRecyclerViewItem>>>()
    var homeData: LiveData<Resource<List<HomeRecyclerViewItem>>> = _homeData

    init {
        getHomePageData()
    }

    private fun getHomePageData() {
        getAnimesUseCase().flowOn(ioDispatcher).catch { e ->
            e.printStackTrace()
        }.onEach {
            _homeData.value = it
        }.launchIn(viewModelScope)

    }
}
