package com.kl3jvi.animity.data.network.anime_service.enime

import com.kl3jvi.animity.data.network.anime_service.base.ApiServiceSingleton
import com.kl3jvi.animity.data.network.anime_service.base.BaseClient
import com.kl3jvi.animity.data.network.anime_service.base.BaseService
import com.kl3jvi.animity.parsers.GoGoParser
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.or1
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class EnimeClient
    @Inject
    constructor(
        apiServiceSingleton: ApiServiceSingleton,
        override val parser: GoGoParser, // Not used here
    ) : BaseClient {
        override var animeService: BaseService =
            apiServiceSingleton.run {
                updateBaseUrl(Constants.ENIME_BASE_URL)
                getApiService(EnimeService::class.java)
            }

        override suspend fun <T> fetchEpisodeMediaUrl(
            header: Map<String, String>,
            episodeUrl: String,
            extra: List<Any?>,
        ): T {
            val animeList =
                mutableListOf<String?>().apply {
                    add((animeService as EnimeService).getEnimeSource(extra.firstOrNull().toString()).url)
                }.toList()
            return animeList as T
        }

        override suspend fun <T> fetchEpisodeList(
            episodeUrl: String,
            extra: List<Any?>,
        ): T =
            (animeService as EnimeService).getEnimeEpisodesIds(
                extra.first()?.toString()?.toInt().or1(),
            ) as T

        override suspend fun <T> getEpisodeTitles(id: Int): T = Unit as T
    }
