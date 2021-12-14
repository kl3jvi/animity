package com.kl3jvi.animity.persistence

import androidx.room.*
import com.kl3jvi.animity.data.model.Content

@Dao
interface EpisodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisode(content: Content)

    @Query("SELECT * FROM Content WHERE episodeUrl =:episodeUrl")
    suspend fun getEpisodeContent(episodeUrl: String): Content

    @Update
    suspend fun updateEpisode(content: Content)

    @Query("SELECT EXISTS(SELECT * FROM Content WHERE episodeUrl = :episodeUrl)")
    suspend fun isEpisodeOnDatabase(episodeUrl: String): Boolean

}