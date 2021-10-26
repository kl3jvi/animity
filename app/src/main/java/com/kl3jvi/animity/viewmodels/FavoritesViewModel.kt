package com.kl3jvi.animity.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kl3jvi.animity.model.database.AnimeRepository
import com.kl3jvi.animity.model.entities.AnimeMetaModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    animeRepository: AnimeRepository
) : ViewModel() {
    val favoriteAnimesList: LiveData<List<AnimeMetaModel>> =
        animeRepository.getFavoriteAnimes.asLiveData()

}