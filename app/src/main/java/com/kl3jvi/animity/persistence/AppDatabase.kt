package com.kl3jvi.animity.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kl3jvi.animity.model.AnimeMetaModel
import com.kl3jvi.animity.utils.Converters

/**
 * The Room database for this app
 */
@Database(entities = [AnimeMetaModel::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun animeDao(): AnimeDao
}
