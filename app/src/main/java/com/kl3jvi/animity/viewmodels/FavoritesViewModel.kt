package com.kl3jvi.animity.viewmodels

import androidx.lifecycle.*
import com.kl3jvi.animity.model.database.AnimeRepository
import com.kl3jvi.animity.model.entities.AnimeMetaModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    val animeRepository: AnimeRepository
) : ViewModel() {
    private val _orderId = MutableLiveData<String>()

    val favoriteAnimesList: LiveData<List<AnimeMetaModel>> =
        animeRepository.getFavoriteAnimes.asLiveData()


    val orderedAnime = Transformations.switchMap(_orderId) { id ->
        animeRepository.getAnimeByOrder(id).asLiveData()
    }

}