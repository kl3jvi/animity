@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kl3jvi.animity.data.repository

import com.kl3jvi.animity.data.model.ui_models.EpisodeEntity
import com.kl3jvi.animity.data.network.anime_service.gogo.GogoAnimeApiClient
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

    override fun getMediaUrl(header: Map<String, String>, url: String): Flow<List<String>> = flow {
        val episodeInfo = parser.parseMediaUrl(
            apiClient.fetchEpisodeMediaUrl(header = header, episodeUrl = url).string()
        )
        val id =
            Regex("id=([^&]+)").find(episodeInfo.vidCdnUrl.orEmpty())?.value?.removePrefix("id=")
        val ajaxResponse = parser.parseEncryptAjax(
            response = apiClient.fetchM3u8Url(
                header = header,
                url = episodeInfo.vidCdnUrl.orEmpty()
            ).string(),
            id = id.orEmpty()
        )
        val streamUrl = "${Constants.REFERER}encrypt-ajax.php?$ajaxResponse"
        val encryptedUrls = parser.parseEncryptedUrls(
            apiClient.fetchM3u8PreProcessor(
                header = header,
                url = streamUrl
            ).string()
        )
        emit(encryptedUrls)
    }.flowOn(ioDispatcher)


    override suspend fun upsertEpisode(episodeEntity: EpisodeEntity) {
        withContext(ioDispatcher) {
            if (episodeDao.isEpisodeOnDatabase(episodeEntity.episodeUrl) && episodeEntity.watchedDuration > 0) {
                episodeDao.updateEpisode(episodeEntity)
            } else {
                episodeDao.insertEpisode(episodeEntity)
            }
        }
    }


    override suspend fun getPlaybackPosition(episodeUrl: String): Flow<EpisodeEntity> =
        withContext(ioDispatcher) {
            if (episodeDao.isEpisodeOnDatabase(episodeUrl)) {
                episodeDao.getEpisodeContent(episodeUrl)
            } else {
                emptyFlow()
            }
        }
}
