@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kl3jvi.animity.data.repository

import android.util.Log
import com.kl3jvi.animity.data.model.ui_models.EpisodeEntity
import com.kl3jvi.animity.data.network.anime_service.base.BaseClient
import com.kl3jvi.animity.domain.repositories.PlayerRepository
import com.kl3jvi.animity.parsers.GoGoParser
import com.kl3jvi.animity.persistence.EpisodeDao
import com.kl3jvi.animity.settings.Settings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlayerRepositoryImpl @Inject constructor(
    apiClients: Map<String, @JvmSuppressWildcards BaseClient>,
    settings: Settings,
    private val episodeDao: EpisodeDao,
    override val parser: GoGoParser,
    private val ioDispatcher: CoroutineDispatcher
) : PlayerRepository {

    private val selectedAnimeProvider: BaseClient? =
        apiClients[settings.selectedProvider.name]

    init {
        Log.e("Selected", settings.selectedProvider.name)
    }

    override fun getMediaUrl(
        header: Map<String, String>,
        url: String,
        extra: List<Any?>
    ): Flow<List<String>> = flow {
        val result = selectedAnimeProvider?.fetchEpisodeMediaUrl(
            header,
            url,
            extra
        ) ?: emptyList<String>()
        emit(result)
    }.catch { it.printStackTrace() }

    override suspend fun upsertEpisode(episodeEntity: EpisodeEntity) {
        withContext(ioDispatcher) {
            if (episodeDao.isEpisodeOnDatabase(episodeEntity.episodeUrl) && episodeEntity.watchedDuration > 0) {
                episodeDao.updateEpisode(episodeEntity)
            } else {
                episodeDao.insertEpisode(episodeEntity)
            }
        }
    }

    override suspend fun getPlaybackPosition(episodeUrl: String): Flow<EpisodeEntity> {
        return episodeDao.getEpisodeContent(episodeUrl)
            .flowOn(ioDispatcher)
            .filter { episodeDao.isEpisodeOnDatabase(episodeUrl) }
    }
}
