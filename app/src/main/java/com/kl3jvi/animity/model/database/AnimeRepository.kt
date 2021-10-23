package com.kl3jvi.animity.model.database

import com.kl3jvi.animity.model.entities.AnimeMetaModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AnimeRepository @Inject constructor(private val dao: AnimeDao) {

    val getFavoriteAnimes: Flow<List<AnimeMetaModel>> = dao.getFavoriteAnimesList()

    suspend fun insertFavDishData(anime: AnimeMetaModel) = dao.insertAnime(anime)

}