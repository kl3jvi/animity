@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kl3jvi.animity.data.repository

import com.kl3jvi.animity.data.model.ui_models.EpisodeEntity
import com.kl3jvi.animity.data.network.anime_service.GogoAnimeApiClient
import com.kl3jvi.animity.domain.repositories.PlayerRepository
import com.kl3jvi.animity.parsers.GoGoParser
import com.kl3jvi.animity.persistence.EpisodeDao
import com.kl3jvi.animity.utils.Constants
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerRepositoryImpl @Inject constructor(
    private val apiClient: GogoAnimeApiClient,
    private val ioDispatcher: CoroutineDispatcher,
    private val episodeDao: EpisodeDao,
    override val parser: GoGoParser
) : PlayerRepository {

    override fun getMediaUrl(
        header: Map<String, String>,
        url: String
    ) = flow {
        val response = parser.parseMediaUrl(
            apiClient.fetchEpisodeMediaUrl(header = header, episodeUrl = url).string()
        )
        emit(response)
    }.flatMapLatest { episodeInfo ->
        flow {
            val id = Regex("id=([^&]+)").find(
                episodeInfo.vidCdnUrl.orEmpty()
            )?.value?.removePrefix("id=")
            val response = parser.parseEncryptAjax(
                response = apiClient.fetchM3u8Url(
                    header = header,
                    url = episodeInfo.vidCdnUrl.orEmpty()
                ).string(),
                id = id.orEmpty()
            )
            val streamUrl = "${Constants.REFERER}encrypt-ajax.php?${response}"
            emit(streamUrl)
        }
    }.flatMapLatest {
        flow {
            val response = parser.parseEncryptedUrls(
                apiClient.fetchM3u8PreProcessor(header = header, url = it).string()
            )
            emit(response)
        }
    }.flowOn(ioDispatcher)

    override suspend fun upsertEpisode(episodeEntity: EpisodeEntity) {
        return withContext(ioDispatcher) {
            val exists = episodeDao.isEpisodeOnDatabase(episodeEntity.episodeUrl) && episodeEntity.watchedDuration > 0
            if (exists) episodeDao.updateEpisode(episodeEntity)
            else episodeDao.insertEpisode(episodeEntity)
        }
    }

    override suspend fun getPlaybackPosition(episodeUrl: String): Flow<EpisodeEntity> {
        return withContext(ioDispatcher) {
            val exists = episodeDao.isEpisodeOnDatabase(episodeUrl)
            if (exists)
                episodeDao.getEpisodeContent(episodeUrl)
            else emptyFlow()
        }
    }
}

