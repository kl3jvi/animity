package com.kl3jvi.animity.ui.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.data.model.ui_models.HomeData
import com.kl3jvi.animity.domain.repositories.fragment_repositories.HomeRepository
import com.kl3jvi.animity.utils.Result
import com.kl3jvi.animity.utils.asResult
import com.kl3jvi.animity.utils.logMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
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
        viewModelScope.launch(Dispatchers.IO) {
            homeRepository.getHomeData().asResult().collect {
                when (it) {
                    is Result.Error -> logMessage(it.exception?.message)
                    Result.Loading -> {}
                    is Result.Success -> _homeData.value = it.data
                }
            }
        }
    }
}
