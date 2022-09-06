package com.kl3jvi.animity.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.data.model.ui_models.EpisodeEntity
import com.kl3jvi.animity.utils.Converters


/**
 *  It's a database class that has two tables, one for anime metadata and one for episode metadata
 *
 * */
@Database(
    entities = [AnimeMetaModel::class, EpisodeEntity::class],
    version = 5,
    exportSchema = true,
//    autoMigrations = [AutoMigration(from = 2, to = 3)]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun episodeDao(): EpisodeDao
}
