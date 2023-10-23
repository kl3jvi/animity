package com.kl3jvi.animity.data.model.ui_models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "anime")
data class LocalAnime(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val coverImageUrl: String,
    val episodesCount: Int = 0,
) {
    fun getEpisodeCountString(): String {
        return when (episodesCount) {
            0 -> "Unknown"
            1 -> "$episodesCount episode"
            else -> "$episodesCount episodes"
        }
    }
}

@Entity(
    tableName = "episodes",
    foreignKeys = [
        ForeignKey(
            entity = LocalAnime::class,
            parentColumns = ["id"],
            childColumns = ["animeId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class LocalEpisode(
    @PrimaryKey
    val episodeUrl: String, // This is the url of the episode
    val animeId: Int,
    val episodeNumber: String,
    val downloaded: Boolean,
)
