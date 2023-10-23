package com.kl3jvi.animity.ui.fragments.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.domain.repositories.FavoriteRepository
import com.kl3jvi.animity.domain.repositories.PersistenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.plus
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class FavoritesViewModel
    @Inject
    constructor(
        private val favoriteRepository: FavoriteRepository,
        private val localStorage: PersistenceRepository,
        private val ioDispatcher: CoroutineDispatcher,
    ) : ViewModel() {
        private var _favoritesList = MutableStateFlow<FavouriteState>(FavouriteState.Loading)
        val favoritesList = _favoritesList.asStateFlow()
        private var job: Job? = null

        init {
            refreshFavorites()
        }

        fun refreshFavorites() {
            favoriteRepository.getFavoriteAnimesFromAniList(localStorage.aniListUserId?.toInt())
                .cachedIn(viewModelScope)
                .onStart { _favoritesList.value = FavouriteState.Loading }
                .catch { _favoritesList.value = FavouriteState.Error(it) }
                .buffer(0)
                .onEach {
                    Log.e("FAVORITES", it.toString())
                    _favoritesList.value = FavouriteState.Success(it)
                }.launchIn(viewModelScope + ioDispatcher)
        }
    }

sealed interface FavouriteState {
    data class Success(val data: PagingData<AniListMedia>) : FavouriteState

    data class Error(val throwable: Throwable) : FavouriteState

    data object Loading : FavouriteState
}
