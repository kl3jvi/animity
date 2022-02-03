package com.kl3jvi.animity.domain.repositories.persistence_repositories

import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.data.model.ui_models.Content
import kotlinx.coroutines.flow.Flow

interface PersistenceRepository {

    suspend fun insertEpisode(content: Content)

    suspend fun updateEpisode(content: Content)

    suspend fun getEpisodeContent(episodeUrl: String): Content

    suspend fun isEpisodeOnDatabase(episodeUrl: String): Boolean

    /** ------------------------------------------------------------------------ */

    suspend fun insertAnime(animeMetaModel: AnimeMetaModel)

    suspend fun deleteAnime(anime: AnimeMetaModel)

    fun getFavoriteAnimesList(): Flow<List<AnimeMetaModel>>

    suspend fun isAnimeOnDatabase(url:String): Boolean

    suspend fun insertAnimeList(list: List<AnimeMetaModel>)
}