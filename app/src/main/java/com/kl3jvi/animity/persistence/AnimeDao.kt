package com.kl3jvi.animity.persistence

import androidx.room.*
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(animeMetaModel: AnimeMetaModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimeList(list: List<AnimeMetaModel>)

    @Delete
    suspend fun deleteAnime(anime: AnimeMetaModel)

    @Query("SELECT * FROM ANIME_TABLE ORDER BY ID")
    fun getFavoriteAnimesList(): Flow<List<AnimeMetaModel>>

    @Query("SELECT EXISTS(SELECT * FROM ANIME_TABLE WHERE categoryUrl = :url)")
    suspend fun isAnimeOnDatabase(url: String): Boolean
}
