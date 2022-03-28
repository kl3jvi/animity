package com.kl3jvi.animity.persistence

import androidx.room.*
import com.kl3jvi.animity.data.model.ui_models.Content
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import kotlinx.coroutines.flow.Flow

@Dao
interface EpisodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisode(content: Content)

    @Update
    suspend fun updateEpisode(content: Content)

    @Query("SELECT * FROM Content WHERE episodeUrl =:episodeUrl")
    fun getEpisodeContent(episodeUrl: String): Flow<Content>


    @Query("SELECT EXISTS(SELECT * FROM Content WHERE episodeUrl = :episodeUrl)")
    suspend fun isEpisodeOnDatabase(episodeUrl: String): Boolean

}