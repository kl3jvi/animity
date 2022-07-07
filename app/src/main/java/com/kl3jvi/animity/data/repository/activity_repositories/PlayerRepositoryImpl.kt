@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kl3jvi.animity.data.repository.activity_repositories

import com.kl3jvi.animity.data.network.anime_service.GogoAnimeApiClient
import com.kl3jvi.animity.domain.repositories.activity_repositories.PlayerRepository
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.GoGoAnime
import com.kl3jvi.animity.utils.logError
import com.kl3jvi.animity.utils.parser.Parser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Suppress("BlockingMethodInNonBlockingContext")
class PlayerRepositoryImpl @Inject constructor(
        @GoGoAnime
    private val apiClient: GogoAnimeApiClient,
    private val ioDispatcher: CoroutineDispatcher,
    override val parser: Parser
) : PlayerRepository {

    override fun getMediaUrl(
        header: Map<String, String>,
        url: String
    ): Flow<List<String>> = flow {
        val response = parser.parseMediaUrl(
            apiClient.fetchEpisodeMediaUrl(header = header, episodeUrl = url).string()
        )
        emit(response)
    }.flowOn(ioDispatcher).flatMapLatest { episodeInfo ->
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
        }.catch { e -> logError(e) }
    }
}

