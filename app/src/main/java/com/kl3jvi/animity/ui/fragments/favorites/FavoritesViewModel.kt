package com.kl3jvi.animity.ui.fragments.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.domain.repositories.FavoriteRepository
import com.kl3jvi.animity.domain.repositories.PersistenceRepository
import com.kl3jvi.animity.utils.Result
import com.kl3jvi.animity.utils.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.plus
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class FavoritesViewModel @Inject constructor(
    favoriteRepository: FavoriteRepository,
    localStorage: PersistenceRepository,
    ioDispatcher: CoroutineDispatcher,
) : ViewModel() {


    val favoritesList: StateFlow<FavoritesUiState> =
        favoriteRepository.getFavoriteAnimesFromAniList(localStorage.aniListUserId?.toInt(), 1)
            .asResult().map {
                when (it) {
                    is Result.Error -> FavoritesUiState.Error()
                    Result.Loading -> FavoritesUiState.Loading
                    is Result.Success -> FavoritesUiState.Success(it.data)
                }
            }.stateIn(
                viewModelScope.plus(context = ioDispatcher),
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = FavoritesUiState.Loading
            )
}


sealed interface FavoritesUiState {
    object Loading : FavoritesUiState
    data class Success(val data: List<AniListMedia>) : FavoritesUiState
    data class Error(val error: Throwable? = null) : FavoritesUiState
}


