package com.kl3jvi.animity.persistence

import androidx.room.*
import com.kl3jvi.animity.data.model.ui_models.EpisodeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EpisodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisode(episodeEntity: EpisodeEntity)

    @Update
    suspend fun updateEpisode(episodeEntity: EpisodeEntity)

    @Query("SELECT * FROM EpisodeEntity WHERE episodeUrl =:episodeUrl")
    fun getEpisodeContent(episodeUrl: String): Flow<EpisodeEntity>

    @Query("SELECT * FROM EpisodeEntity WHERE malId = :malId")
    fun getEpisodesByAnime(malId: Int): Flow<List<EpisodeEntity>>

    @Query("SELECT EXISTS(SELECT * FROM EpisodeEntity WHERE episodeUrl = :episodeUrl)")
    suspend fun isEpisodeOnDatabase(episodeUrl: String): Boolean
}
