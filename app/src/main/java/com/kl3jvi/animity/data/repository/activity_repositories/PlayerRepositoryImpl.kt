package com.kl3jvi.animity.data.repository.activity_repositories

import com.kl3jvi.animity.data.network.anime_service.GogoAnimeApiClient
import com.kl3jvi.animity.domain.repositories.activity_repositories.PlayerRepository
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.parser.Parser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Suppress("BlockingMethodInNonBlockingContext")
class PlayerRepositoryImpl @Inject constructor(
    private val apiClient: GogoAnimeApiClient,
    private val ioDispatcher: CoroutineDispatcher,
    override val parser: Parser
) : PlayerRepository {

    override fun fetchEpisodeMediaUrl(
        header: Map<String, String>,
        url: String
    ) = flow {
        val response = parser.parseMediaUrl(
            apiClient.fetchEpisodeMediaUrl(header = header, episodeUrl = url).string()
        )
        emit(response)
    }

    override fun fetchM3u8Url(
        header: Map<String, String>,
        url: String
    ) = flow {
        val response = parser.parseEncryptedUrls(
            apiClient.fetchM3u8PreProcessor(header = header, url = url).string()
        )
        emit(response)
    }

    override fun fetchEncryptedAjaxUrl(
        header: Map<String, String>,
        url: String,
        id: String
    ) = flow {
        val response = parser.parseEncryptAjax(
            response = apiClient.fetchM3u8Url(
                header = header,
                url = url
            ).string(),
            id = id
        )
        val streamUrl = "${Constants.REFERER}encrypt-ajax.php?${response}"
        emit(streamUrl)
    }
}

