package com.kl3jvi.animity.data.repository

import com.kl3jvi.animity.domain.repositories.PlayerRepository
import com.kl3jvi.animity.data.model.EpisodeInfo
import com.kl3jvi.animity.data.network.AnimeApiClient
import com.kl3jvi.animity.utils.parser.HtmlParser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerRepositoryImpl @Inject constructor(
    private val apiClient: AnimeApiClient
) : PlayerRepository {
    override val parser: HtmlParser
        get() = HtmlParser

    override suspend fun fetchEpisodeMediaUrl(
        header: Map<String, String>,
        url: String
    ): EpisodeInfo {
        return parser.parseMediaUrl(apiClient.fetchEpisodeMediaUrl(header, url).string())
    }

    override suspend fun fetchM3u8Url(header: Map<String, String>, url: String): String {
        return parser.parseM3U8Url(apiClient.fetchM3u8Url(header, url).string()) ?: ""
    }
}
