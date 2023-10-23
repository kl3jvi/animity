package com.kl3jvi.animity.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kl3jvi.animity.data.model.ui_models.LocalAnime
import com.kl3jvi.animity.data.model.ui_models.LocalEpisode
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalDownloadsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(anime: LocalAnime)

    @Query(
        """
        SELECT DISTINCT a.* 
        FROM anime AS a
        INNER JOIN episodes AS e ON a.id = e.animeId
    """,
    )
    fun getAllAnime(): Flow<List<LocalAnime>>

    @Query("SELECT * FROM anime WHERE id = :animeId")
    suspend fun getAnimeById(animeId: Int): LocalAnime?

    @Query("UPDATE anime SET episodesCount = episodesCount + 1 WHERE id = :animeId")
    suspend fun incrementDownloadedEpisodesCount(animeId: Int)
}

@Dao
interface LocalEpisodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(episode: LocalEpisode)

    @Query("SELECT * FROM episodes WHERE animeId = :animeId")
    fun getEpisodesForAnime(animeId: Int): Flow<List<LocalEpisode>>

    @Query("UPDATE episodes SET downloaded = :downloaded WHERE episodeUrl = :episodeUrl")
    suspend fun updateEpisodeDownloadStatus(
        episodeUrl: String,
        downloaded: Boolean,
    )
}
