package com.kl3jvi.animity.ui.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.analytics.Analytics
import com.kl3jvi.animity.domain.repositories.HomeRepository
import com.kl3jvi.animity.utils.mapToUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    homeRepository: HomeRepository,
    val analytics: Analytics,
    ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    val homeDataUiState = homeRepository.getHomeData()
        .mapToUiState(viewModelScope + ioDispatcher)
}
