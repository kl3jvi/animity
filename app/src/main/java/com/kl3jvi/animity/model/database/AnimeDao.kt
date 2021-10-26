package com.kl3jvi.animity.model.database

import androidx.room.*
import com.kl3jvi.animity.model.entities.AnimeMetaModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(animeMetaModel: AnimeMetaModel)

    @Delete
    suspend fun deleteAnime(anime: AnimeMetaModel)

    @Query("SELECT * FROM ANIME_TABLE ORDER BY ID")
    fun getFavoriteAnimesList(): Flow<List<AnimeMetaModel>>

    @Query("SELECT EXISTS(SELECT * FROM ANIME_TABLE WHERE id = :userId)")
    suspend fun isAnimeOnDatabase(userId: Int): Boolean

    @Query("SELECT * FROM ANIME_TABLE ORDER BY :filterType")
    fun getAnimeOrdered(filterType: String): Flow<List<AnimeMetaModel>>

}
