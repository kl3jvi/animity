package com.kl3jvi.animity.model.database

import androidx.room.*
import com.kl3jvi.animity.model.entities.AnimeMetaModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(animeMetaModel: AnimeMetaModel)

    @Query("SELECT * FROM ANIME_TABLE ORDER BY ID")
    fun getFavoriteAnimesList(): Flow<List<AnimeMetaModel>>

    @Update
    suspend fun updateAnime(anime: AnimeMetaModel)

    @Query("SELECT EXISTS(SELECT * FROM anime_table WHERE id = :userId)")
    suspend fun isAnimeOnDatabase(userId: Int): Boolean

    @Delete
    suspend fun deleteAnime(anime: AnimeMetaModel)

}
