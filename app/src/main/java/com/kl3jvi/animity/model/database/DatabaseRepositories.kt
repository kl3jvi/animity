package com.kl3jvi.animity.model.database

import com.kl3jvi.animity.model.entities.AnimeMetaModel

class DatabaseRepositories(private val animeDao: AnimeDao) {

    suspend fun insertAnime(animeMetaModel: AnimeMetaModel) {
        animeDao.insertAnimeModel(animeMetaModel)
    }


}