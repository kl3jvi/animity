package com.kl3jvi.animity.model.database

import androidx.room.*
import com.kl3jvi.animity.model.entities.AnimeMetaModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(animeMetaModel: AnimeMetaModel)

    @Query("SELECT * FROM ANIME_TABLE WHERE favorite_anime = 1")
    fun getFavoriteAnimesList(): Flow<List<AnimeMetaModel>>

    @Update
    suspend fun updateAnime(anime: AnimeMetaModel)

    @Query("SELECT COUNT() FROM anime_table WHERE id = :id")
    suspend fun count(id: Int): Int

    @Delete
    suspend fun deleteAnime(anime: AnimeMetaModel)


}
