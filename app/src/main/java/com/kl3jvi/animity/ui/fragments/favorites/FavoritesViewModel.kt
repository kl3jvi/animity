package com.kl3jvi.animity.ui.fragments.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.FavoritesAnimeQuery
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.data.repository.fragment_repositories.UserRepositoryImpl
import com.kl3jvi.animity.data.repository.persistence_repository.PersistenceRepositoryImpl
import com.kl3jvi.animity.domain.use_cases.GetFavoriteAnimesUseCase
import com.kl3jvi.animity.domain.use_cases.GetGogoUrlFromFavoritesId
import com.kl3jvi.animity.domain.use_cases.GetUserSessionUseCase
import com.kl3jvi.animity.persistence.AnimeRepository
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
    private val animeRepository: AnimeRepository,
    private val getFavoriteAnimesUseCase: GetFavoriteAnimesUseCase,
    private val getUserSessionUseCase: GetUserSessionUseCase,
    private val persistenceRepository: PersistenceRepositoryImpl,
    private val userRepo: UserRepositoryImpl,
    private val ioDispatcher: CoroutineDispatcher,
    private val getGogoUrlFromFavoritesId: GetGogoUrlFromFavoritesId

) : ViewModel() {

    private var _favoriteAniListAnimeList =
        MutableStateFlow<ApolloResponse<FavoritesAnimeQuery.Data>?>(null)
    val favoriteAniListAnimeList = _favoriteAniListAnimeList.asStateFlow()

    private val _favoriteFromDatabase = MutableStateFlow(emptyList<AnimeMetaModel>())
    val favoriteFromDatabase = _favoriteFromDatabase.asStateFlow()

    val shouldRefresh = MutableStateFlow(true)

    init {
        getFavoriteAnimes()
    }

    private fun getFavoriteAnimes() {
        viewModelScope.launch(Dispatchers.IO) {
            shouldRefresh.collect { shouldRefresh ->
                if (shouldRefresh) {
                    getUserSessionUseCase().flatMapLatest {
                        getFavoriteAnimesUseCase(it.data?.viewer?.id, 1)
                    }.flowOn(ioDispatcher)
                        .catch { e -> Log.e("Error", e.message.orEmpty()) }
                        .collect {
                            _favoriteAniListAnimeList.value = it
                        }
                }
                animeRepository.getFavoriteAnimes.collect {
                    _favoriteFromDatabase.value = it
                }
            }
        }
    }

}
