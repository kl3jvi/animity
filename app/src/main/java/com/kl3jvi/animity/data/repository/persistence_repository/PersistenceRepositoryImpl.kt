package com.kl3jvi.animity.data.repository.persistence_repository

import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.data.model.ui_models.Content
import com.kl3jvi.animity.domain.repositories.persistence_repositories.PersistenceRepository
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

    override suspend fun getEpisodeContent(episodeUrl: String): Flow<Content> =
        episodeDao.getEpisodeContent(episodeUrl)

    override suspend fun isEpisodeOnDatabase(episodeUrl: String): Boolean =
        episodeDao.isEpisodeOnDatabase(episodeUrl)

    /** ------------------------------------------------------------------------ */

    override suspend fun insertAnime(animeMetaModel: AnimeMetaModel) =
        animeDao.insertAnime(animeMetaModel)

    override suspend fun deleteAnime(anime: AnimeMetaModel) = animeDao.deleteAnime(anime)

    override fun getFavoriteAnimesList(): Flow<List<AnimeMetaModel>> =
        animeDao.getFavoriteAnimesList()

    override suspend fun isAnimeOnDatabase(url: String): Boolean =
        animeDao.isAnimeOnDatabase(url)

    override suspend fun insertAnimeList(list: List<AnimeMetaModel>) {
        animeDao.insertAnimeList(list)
    }

}