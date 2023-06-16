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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.plus
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class FavoritesViewModel @Inject constructor(
    favoriteRepository: FavoriteRepository,
    localStorage: PersistenceRepository,
    ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    val favoritesList: StateFlow<UiResult<PagingData<AniListMedia>>> =
        favoriteRepository.getFavoriteAnimesFromAniList(
            userId = localStorage.aniListUserId?.toInt(),
            page = 1
        ).cachedIn(viewModelScope)
            .mapToUiState(viewModelScope + ioDispatcher)
}
