package com.kl3jvi.animity.domain.repositories

import com.kl3jvi.animity.data.model.ui_models.EpisodeEntity
import com.kl3jvi.animity.parsers.Providers
import kotlinx.coroutines.flow.Flow

interface PersistenceRepository {

    suspend fun insertEpisode(episodeEntity: EpisodeEntity)
    suspend fun updateEpisode(episodeEntity: EpisodeEntity)
    suspend fun getEpisodeContent(episodeUrl: String): Flow<EpisodeEntity>
    suspend fun isEpisodeOnDatabase(episodeUrl: String): Boolean

    var bearerToken: String?
    var refreshToken: String?
    var guestToken: String?
    var aniListUserId: String?
    var dns: Int?

    var iv: String?
    var key: String?
    var secondKey: String?

    var selectedProvider: Providers?

    fun clearStorage()
}