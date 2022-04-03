package com.kl3jvi.animity.ui.fragments.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.domain.repositories.persistence_repositories.LocalStorage
import com.kl3jvi.animity.domain.use_cases.GetFavoriteAnimesUseCase
import com.kl3jvi.animity.utils.NetworkResource
import com.kl3jvi.animity.utils.logError
import com.kl3jvi.animity.utils.logMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoriteAnimesUseCase: GetFavoriteAnimesUseCase,
    private val dataStore: LocalStorage,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private var _favoriteAniListAnimeList =
        MutableStateFlow<List<AniListMedia>?>(null)
    val favoriteAniListAnimeList = _favoriteAniListAnimeList.asStateFlow()

    val shouldRefresh = MutableStateFlow(true)

    init {
        getFavoriteAnimes()
    }

    private fun getFavoriteAnimes() {
        viewModelScope.launch(Dispatchers.IO) {
            shouldRefresh.collectLatest { _ ->
                getFavoriteAnimesUseCase(dataStore.aniListUserId?.toInt(), 1)
                    .flowOn(ioDispatcher)
                    .catch { e -> logError(e) }
                    .collect {
                        when (it) {
                            is NetworkResource.Failed -> {
                                logMessage(it.message)
                            }
                            is NetworkResource.Success -> {
                                _favoriteAniListAnimeList.value = it.data
                            }
                        }

                    }
            }
        }
    }

}


