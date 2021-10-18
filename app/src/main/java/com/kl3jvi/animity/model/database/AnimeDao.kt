package com.kl3jvi.animity.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kl3jvi.animity.model.entities.AnimeMetaModel
import kotlinx.coroutines.flow.Flow


@Dao
interface AnimeDao {
    @Insert
    suspend fun insertAnimeModel(animeMetaModel: AnimeMetaModel)

    @Update
    suspend fun updateFavDishDetails(animeMetaModel: AnimeMetaModel)

    @Query("SELECT * FROM ANIME_TABLE ORDER BY ID")
    fun getAllDishesList(): Flow<List<AnimeMetaModel>>
}