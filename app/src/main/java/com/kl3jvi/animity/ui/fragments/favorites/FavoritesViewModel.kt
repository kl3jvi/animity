package com.kl3jvi.animity.ui.fragments.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kl3jvi.animity.domain.use_cases.GetFavoriteAnimesUseCase
import com.kl3jvi.animity.domain.use_cases.GetUserSessionUseCase
import com.kl3jvi.animity.persistence.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
//    animeRepository: AnimeRepository,
    private val getFavoriteAnimesUseCase: GetFavoriteAnimesUseCase,
    private val getUserSessionUseCase: GetUserSessionUseCase
) : ViewModel() {
//    val favoriteAnimesList: LiveData<List<AnimeMetaModel>> =
//        animeRepository.getFavoriteAnimes.asLiveData()

    val favoriteAnimesList = getUserSessionUseCase().flatMapLatest {
        getFavoriteAnimesUseCase(it.data?.viewer?.id, 1)
    }.asLiveData()
}
