package com.kl3jvi.animity.data.network.anime_service.bili

import com.kl3jvi.animity.data.network.anime_service.base.ApiServiceSingleton
import com.kl3jvi.animity.data.network.anime_service.base.BaseClient
import com.kl3jvi.animity.data.network.anime_service.base.BaseService
import com.kl3jvi.animity.parsers.AllAnimeParser
import com.kl3jvi.animity.utils.Constants
import javax.inject.Inject

class BiliAnimeApiClient @Inject constructor(
    apiServiceSingleton: ApiServiceSingleton,
    override val parser: AllAnimeParser,
) : BaseClient {

    override var animeService: BaseService = apiServiceSingleton.run {
        updateBaseUrl(Constants.BILI_URL)
        getApiService(BiliAnimeService::class.java)
    }

    override suspend fun <T> fetchEpisodeMediaUrl(
        header: Map<String, String>,
        episodeUrl: String,
        extra: List<Any?>,
    ): T {
        TODO("Not yet implemented")
    }

    override suspend fun <T> fetchEpisodeList(episodeUrl: String, extra: List<Any?>): T {
        TODO("Not yet implemented")
    }

    override suspend fun <T> getEpisodeTitles(id: Int): T {
        TODO("Not yet implemented")
    }
}
