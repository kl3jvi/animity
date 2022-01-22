package com.kl3jvi.animity.data.repository

import com.kl3jvi.animity.data.model.AnimeMetaModel
import com.kl3jvi.animity.data.model.Content
import com.kl3jvi.animity.domain.repositories.PersistenceRepository
import com.kl3jvi.animity.persistence.AnimeDao
import com.kl3jvi.animity.persistence.EpisodeDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PersistenceRepositoryImpl @Inject constructor(
    private val animeDao: AnimeDao,
    private val episodeDao: EpisodeDao
) : PersistenceRepository {

    override suspend fun insertEpisode(content: Content) = episodeDao.insertEpisode(content)


    override suspend fun updateEpisode(content: Content) = episodeDao.updateEpisode(content)

    override suspend fun getEpisodeContent(episodeUrl: String): Content =
        episodeDao.getEpisodeContent(episodeUrl)

    override suspend fun isEpisodeOnDatabase(episodeUrl: String): Boolean =
        episodeDao.isEpisodeOnDatabase(episodeUrl)

    /** ------------------------------------------------------------------------ */

    override suspend fun insertAnime(animeMetaModel: AnimeMetaModel) =
        animeDao.insertAnime(animeMetaModel)

    override suspend fun deleteAnime(anime: AnimeMetaModel) = animeDao.deleteAnime(anime)

    override fun getFavoriteAnimesList(): Flow<List<AnimeMetaModel>> =
        animeDao.getFavoriteAnimesList()

    override suspend fun isAnimeOnDatabase(id: Int): Boolean =
        animeDao.isAnimeOnDatabase(id)

}