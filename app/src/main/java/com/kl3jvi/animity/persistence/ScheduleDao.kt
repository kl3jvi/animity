package com.kl3jvi.animity.persistence

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kl3jvi.animity.data.model.ui_models.AniListMediaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnime(episodeEntity: AniListMediaEntity)

    @Delete
    suspend fun deleteAnime(episodeEntity: AniListMediaEntity)

    @Query("SELECT * FROM schedule_table WHERE idAniList =:aniListId")
    fun getAnimeScheduleStatus(aniListId: Int): Flow<AniListMediaEntity?>

    @Query("SELECT * FROM schedule_table")
    fun getAllScheduled(): Flow<List<AniListMediaEntity?>?>

    @Query("SELECT EXISTS(SELECT * FROM schedule_table WHERE idAniList = :aniListId LIMIT 1)")
    suspend fun isAnimeOnDatabase(aniListId: Int): Boolean
}
