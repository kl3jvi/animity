package com.kl3jvi.animity.persistence

import com.kl3jvi.animity.data.model.AnimeMetaModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeRepository @Inject constructor(private val dao: AnimeDao) {

    val getFavoriteAnimes: Flow<List<AnimeMetaModel>> = dao.getFavoriteAnimesList()

    suspend fun insertFavoriteAnime(anime: AnimeMetaModel) = dao.insertAnime(anime)

    suspend fun checkIfAnimeIsOnDatabase(id: Int) = dao.isAnimeOnDatabase(id)

    suspend fun deleteAnime(anime: AnimeMetaModel) = dao.deleteAnime(anime)
}
