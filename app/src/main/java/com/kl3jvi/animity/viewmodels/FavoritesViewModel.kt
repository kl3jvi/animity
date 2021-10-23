package com.kl3jvi.animity.viewmodels

import androidx.lifecycle.ViewModel
import com.kl3jvi.animity.model.database.AnimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val animeRepository: AnimeRepository
) : ViewModel() {

}