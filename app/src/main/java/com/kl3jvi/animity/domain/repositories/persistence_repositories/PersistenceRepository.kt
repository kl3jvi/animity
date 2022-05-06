package com.kl3jvi.animity.domain.repositories.persistence_repositories

import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.data.model.ui_models.Content
import kotlinx.coroutines.flow.Flow

interface PersistenceRepository {

    /**
     * This function inserts a new episode into the database.
     *
     * @param content Content - The content object that you want to insert into the database.
     */
    suspend fun insertEpisode(content: Content)

    /**
     * This function updates an episode in the database.
     *
     * @param content Content - The content object that you want to update.
     */
    suspend fun updateEpisode(content: Content)

    /**
     * It returns a Flow of Content objects, and it's a suspend function
     *
     * @param episodeUrl The URL of the episode you want to get the content from.
     */
    suspend fun getEpisodeContent(episodeUrl: String): Flow<Content>

    /**
     * This function checks if the episode is already on the database
     *
     * @param episodeUrl The url of the episode you want to check if it's on the database.
     */
    suspend fun isEpisodeOnDatabase(episodeUrl: String): Boolean

    /** ------------------------------------------------------------------------ */

    /**
     * This function inserts an anime into the database.
     *
     * @param animeMetaModel The AnimeMetaModel object that you want to insert into the database.
     */
    suspend fun insertAnime(animeMetaModel: AnimeMetaModel)

    suspend fun deleteAnime(anime: AnimeMetaModel)

    fun getFavoriteAnimesList(): Flow<List<AnimeMetaModel>>

    suspend fun isAnimeOnDatabase(url:String): Boolean

    suspend fun insertAnimeList(list: List<AnimeMetaModel>)
}