package com.kl3jvi.animity.ui.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.data.model.ui_models.HomeData
import com.kl3jvi.animity.domain.repositories.HomeRepository
import com.kl3jvi.animity.utils.Result
import com.kl3jvi.animity.utils.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    homeRepository: HomeRepository,
) : ViewModel() {

    val homeDataUiState: StateFlow<HomeDataUiState> = homeRepository.getHomeData()
        .asResult()
        .map { homeData ->
            when (homeData) {
                is Result.Error -> HomeDataUiState.Error(homeData.exception)
                is Result.Loading -> HomeDataUiState.Loading
                is Result.Success -> HomeDataUiState.Success(homeData.data)
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            HomeDataUiState.Loading
        )

}

sealed interface HomeDataUiState {
    data class Success(val data: HomeData) : HomeDataUiState
    object Loading : HomeDataUiState
    data class Error(val exception: Throwable?) : HomeDataUiState
}



