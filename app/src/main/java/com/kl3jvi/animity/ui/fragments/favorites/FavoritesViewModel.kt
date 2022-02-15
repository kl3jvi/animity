package com.kl3jvi.animity.ui.fragments.favorites

import androidx.lifecycle.*
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.data.repository.fragment_repositories.UserRepositoryImpl
import com.kl3jvi.animity.data.repository.persistence_repository.PersistenceRepositoryImpl
import com.kl3jvi.animity.domain.use_cases.GetFavoriteAnimesUseCase
import com.kl3jvi.animity.domain.use_cases.GetUserSessionUseCase
import com.kl3jvi.animity.persistence.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class FavoritesViewModel @Inject constructor(
    animeRepository: AnimeRepository,
    private val getFavoriteAnimesUseCase: GetFavoriteAnimesUseCase,
    private val getUserSessionUseCase: GetUserSessionUseCase,
    private val persistenceRepository: PersistenceRepositoryImpl,
    private val userRepo: UserRepositoryImpl,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _listOfIds = MutableLiveData<List<Int>>()
    val listOfIds: LiveData<List<Int>> = _listOfIds

    val favoriteFromDatabase: LiveData<List<AnimeMetaModel>> =
        animeRepository.getFavoriteAnimes.asLiveData(ioDispatcher + viewModelScope.coroutineContext)

    val favoriteAnimesList = getUserSessionUseCase().flatMapLatest {
        getFavoriteAnimesUseCase(it.data?.viewer?.id, 1)
    }.flowOn(ioDispatcher)

    fun insertRemoteToLocalDb(list: List<AnimeMetaModel>) = viewModelScope.launch {
        persistenceRepository.insertAnimeList(list)
    }

    fun isDataSynced(): Boolean {
        return userRepo.isFavoritesSynced
    }

    fun syncData(sync: String?) {
        userRepo.setSyncData(sync)
    }
}
