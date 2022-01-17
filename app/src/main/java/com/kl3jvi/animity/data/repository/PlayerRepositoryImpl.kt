package com.kl3jvi.animity.data.repository

import com.kl3jvi.animity.data.model.EpisodeInfo
import com.kl3jvi.animity.data.network.AnimeApiClient
import com.kl3jvi.animity.domain.repositories.PlayerRepository
import com.kl3jvi.animity.utils.parser.HtmlParser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Suppress("BlockingMethodInNonBlockingContext")
class PlayerRepositoryImpl @Inject constructor(
    private val apiClient: AnimeApiClient,
    private val ioDispatcher: CoroutineDispatcher
) : PlayerRepository {
    override val parser: HtmlParser
        get() = HtmlParser

    override suspend fun fetchEpisodeMediaUrl(
        header: Map<String, String>,
        url: String
    ): EpisodeInfo = withContext(ioDispatcher) {
        parser.parseMediaUrl(apiClient.fetchEpisodeMediaUrl(header = header, url = url).string())
    }

    override suspend fun fetchM3u8Url(
        header: Map<String, String>,
        url: String
    ): ArrayList<String> =
        withContext(ioDispatcher) {
            parser.parseEncryptedUrls(
                apiClient.fetchM3u8PreProcessor(header = header, url = url).string()
            )
        }

    override suspend fun fetchEncryptedAjaxUrl(header: Map<String, String>, url: String): String =
        withContext(ioDispatcher) {
            parser.parseEncryptAjax(
                apiClient.fetchM3u8Url(
                    header = header,
                    url = url
                ).string()
            )
        }
}

