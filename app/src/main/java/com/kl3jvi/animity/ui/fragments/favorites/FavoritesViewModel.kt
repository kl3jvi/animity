package com.kl3jvi.animity.ui.fragments.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.domain.repositories.FavoriteRepository
import com.kl3jvi.animity.domain.repositories.PersistenceRepository
import com.kl3jvi.animity.utils.UiResult
import com.kl3jvi.animity.utils.mapToUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val localStorage: PersistenceRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private var _favoritesList =
        MutableStateFlow<UiResult<PagingData<AniListMedia>>>(UiResult.Loading)
    val favoritesList: StateFlow<UiResult<PagingData<AniListMedia>>> = _favoritesList

    init {
        refreshFavorites()
    }

    fun refreshFavorites() {
        viewModelScope.launch(ioDispatcher) {
            favoriteRepository.getFavoriteAnimesFromAniList(localStorage.aniListUserId?.toInt())
                .cachedIn(viewModelScope)
                .mapToUiState(viewModelScope)
                .collect { _favoritesList.value = it }
        }
    }
}
