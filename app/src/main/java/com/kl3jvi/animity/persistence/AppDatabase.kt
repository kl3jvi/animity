package com.kl3jvi.animity.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kl3jvi.animity.data.model.ui_models.AniListMediaEntity
import com.kl3jvi.animity.data.model.ui_models.EpisodeEntity
import com.kl3jvi.animity.data.model.ui_models.LocalAnime
import com.kl3jvi.animity.data.model.ui_models.LocalEpisode

@Database(
    entities = [
        EpisodeEntity::class,
        AniListMediaEntity::class,
        LocalAnime::class,
        LocalEpisode::class,
    ],
    version = 8,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun episodeDao(): EpisodeDao

    abstract fun localAnimeDao(): LocalDownloadsDao

    abstract fun localEpisodeDao(): LocalEpisodeDao
}
