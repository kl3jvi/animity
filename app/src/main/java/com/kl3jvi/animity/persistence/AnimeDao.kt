package com.kl3jvi.animity.persistence

import androidx.room.*
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {

    /**
     * This function inserts the animeMetaModel object into the anime_meta_table table
     *
     * @param animeMetaModel The object that you want to insert into the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(animeMetaModel: AnimeMetaModel)

    /**
     * This function takes a list of AnimeMetaModel objects and inserts them into the database
     *
     * @param list List<AnimeMetaModel> - The list of AnimeMetaModel objects to be inserted into the
     * database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimeList(list: List<AnimeMetaModel>)

    /**
     * Delete an anime from the database.
     *
     * @param anime AnimeMetaModel - The anime to be deleted
     */
    @Delete
    suspend fun deleteAnime(anime: AnimeMetaModel)

    /**
     * This function returns a Flow of List of AnimeMetaModel
     */

    @Query("SELECT * FROM ANIME_TABLE ORDER BY ID")
    fun getFavoriteAnimesList(): Flow<List<AnimeMetaModel>>

    /**
     * This function checks if the anime is already on the database
     *
     * @param url The url of the category you want to check if it's on the database.
     */
    @Query("SELECT EXISTS(SELECT * FROM ANIME_TABLE WHERE categoryUrl = :url)")
    suspend fun isAnimeOnDatabase(url: String): Boolean
}

