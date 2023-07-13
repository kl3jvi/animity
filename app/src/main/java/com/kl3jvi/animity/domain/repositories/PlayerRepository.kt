package com.kl3jvi.animity.domain.repositories

import com.kl3jvi.animity.data.model.ui_models.EpisodeEntity
import com.kl3jvi.animity.parsers.GoGoParser
import com.kl3jvi.animity.utils.Constants.Companion.getNetworkHeader
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    val parser: GoGoParser

    fun getMediaUrl(
        header: Map<String, String> = getNetworkHeader(),
        url: String,
        extra: List<Any?> = emptyList()
    ): Flow<List<List<String>>>

    suspend fun upsertEpisode(episodeEntity: EpisodeEntity)
    suspend fun getPlaybackPosition(episodeUrl: String): Flow<EpisodeEntity>
}
