package com.kl3jvi.animity.ui.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.data.model.ui_models.HomeData
import com.kl3jvi.animity.domain.use_cases.GetAnimesUseCase
import com.kl3jvi.animity.utils.NetworkResource
import com.kl3jvi.animity.utils.logMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAnimesUseCase: GetAnimesUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var _homeData = MutableStateFlow(HomeData())
    var homeData = _homeData.asStateFlow()

    init {
        getHomePageData()
    }

    /**
     * It fetches data from the server and updates the UI.
     */
    private fun getHomePageData() {
        viewModelScope.launch(ioDispatcher) {
            getAnimesUseCase().collect {
                when (it) {
                    is NetworkResource.Failed -> {
                        /* A function that logs the message to the console. */
                        logMessage(it.message)
                    }
                    is NetworkResource.Success -> {
                        _homeData.value = it.data
                    }
                }
            }
        }
    }
}
