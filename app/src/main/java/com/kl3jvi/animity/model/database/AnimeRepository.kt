package com.kl3jvi.animity.model.database

import com.kl3jvi.animity.model.entities.AnimeMetaModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeRepository @Inject constructor(private val dao: AnimeDao) {

    val getFavoriteAnimes: Flow<List<AnimeMetaModel>> = dao.getFavoriteAnimesList()

    suspend fun insertFavoriteAnime(anime: AnimeMetaModel) = dao.insertAnime(anime)

    suspend fun updateAnime(anime: AnimeMetaModel) = dao.updateAnime(anime)

    suspend fun checkIfAnimeIsOnDatabase(id: Int) = dao.count(id) > 0

    suspend fun deleteAnime(anime: AnimeMetaModel) = dao.deleteAnime(anime)


}