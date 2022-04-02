package com.kl3jvi.animity.ui.fragments.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.FavoritesAnimeQuery
import com.kl3jvi.animity.domain.use_cases.GetFavoriteAnimesUseCase
import com.kl3jvi.animity.domain.use_cases.GetUserSessionUseCase
import com.kl3jvi.animity.utils.logError
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
    private val getUserSessionUseCase: GetUserSessionUseCase,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private var _favoriteAniListAnimeList =
        MutableStateFlow<ApolloResponse<FavoritesAnimeQuery.Data>?>(null)
    val favoriteAniListAnimeList = _favoriteAniListAnimeList.asStateFlow()

    val shouldRefresh = MutableStateFlow(true)

    init {
        getFavoriteAnimes()
    }

    private fun getFavoriteAnimes() {
        viewModelScope.launch(Dispatchers.IO) {
            shouldRefresh.collectLatest { _ ->
                getUserSessionUseCase().flatMapLatest {
                    getFavoriteAnimesUseCase(it.data?.viewer?.id, 1)
                }.flowOn(ioDispatcher)
                    .catch { e -> logError(e) }
                    .collect {
                        _favoriteAniListAnimeList.value = it
                    }
            }
        }
    }

}


