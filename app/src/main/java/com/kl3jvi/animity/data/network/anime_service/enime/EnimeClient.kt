package com.kl3jvi.animity.data.network.anime_service.enime

import com.kl3jvi.animity.data.network.anime_service.base.ApiServiceSingleton
import com.kl3jvi.animity.data.network.anime_service.base.BaseClient
import com.kl3jvi.animity.data.network.anime_service.base.BaseService
import com.kl3jvi.animity.parsers.GoGoParser
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.or1
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class EnimeClient @Inject constructor(
    apiServiceSingleton: ApiServiceSingleton,
    override val parser: GoGoParser
) : BaseClient {
    override var animeService: BaseService = apiServiceSingleton.run {
        updateBaseUrl(Constants.ENIME_BASE_URL)
        getApiService(EnimeService::class.java)
    }

    override suspend fun <T> fetchEpisodeMediaUrl(
        header: Map<String, String>,
        episodeUrl: String,
        extra: List<Any?>
    ): T = getEnimeSource(extra.first().toString()) as T

    override suspend fun <T> fetchEpisodeList(
        episodeUrl: String,
        extra: List<Any?>
    ): T = getEnimeEpisodesIds(extra.first()?.toString()?.toInt().or1()) as T

    override suspend fun <T> getEpisodeTitles(id: Int): T {
        TODO("Not yet implemented")
    }

    private suspend fun getEnimeEpisodesIds(malId: Int) =
        (animeService as EnimeService).getEnimeEpisodesIds(malId)

    suspend fun getEnimeSource(source: String) =
        (animeService as EnimeService).getEnimeSource(source)
}
