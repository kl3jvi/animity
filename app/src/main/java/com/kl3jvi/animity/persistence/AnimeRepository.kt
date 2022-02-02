package com.kl3jvi.animity.persistence

import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeRepository @Inject constructor(private val dao: AnimeDao) {

    val getFavoriteAnimes: Flow<List<AnimeMetaModel>> = dao.getFavoriteAnimesList()

    suspend fun insertFavoriteAnime(anime: AnimeMetaModel) = dao.insertAnime(anime)

    suspend fun checkIfAnimeIsOnDatabase(title: String) = dao.isAnimeOnDatabase(title)

    suspend fun deleteAnime(anime: AnimeMetaModel) = dao.deleteAnime(anime)
}
