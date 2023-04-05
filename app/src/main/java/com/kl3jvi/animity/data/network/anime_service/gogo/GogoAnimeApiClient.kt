package com.kl3jvi.animity.data.network.anime_service.gogo

import com.kl3jvi.animity.data.network.anime_service.base.ApiServiceSingleton
import com.kl3jvi.animity.data.network.anime_service.base.BaseClient
import com.kl3jvi.animity.data.network.anime_service.base.BaseService
import com.kl3jvi.animity.parsers.GoGoParser
import com.kl3jvi.animity.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class GogoAnimeApiClient @Inject constructor(
    apiServiceSingleton: ApiServiceSingleton,
    override val parser: GoGoParser
) : BaseClient {

    override var animeService: BaseService =
        apiServiceSingleton.run {
            updateBaseUrl(Constants.GOGO_BASE_URL)
            getApiService(GogoAnimeService::class.java)
        }

    private suspend fun fetchAnimeInfo(
        header: Map<String, String>,
        episodeUrl: String
    ) = withContext(Dispatchers.IO) {
        (animeService as GogoAnimeService).fetchAnimeInfo(
            header,
            episodeUrl
        ).string()
    }

    override suspend fun <T> fetchEpisodeList(
        episodeUrl: String,
        extra: List<Any?>
    ): T {
        val animeInfo = fetchAnimeInfo(Constants.getNetworkHeader(), episodeUrl = episodeUrl)
            .run { parser.parseAnimeInfo(this) }

        return (animeService as GogoAnimeService).fetchEpisodeList(
            header = Constants.getNetworkHeader(),
            id = animeInfo.id,
            endEpisode = animeInfo.endEpisode,
            alias = animeInfo.alias
        ) as T
    }

    override suspend fun <T> getEpisodeTitles(id: Int): T =
        (animeService as GogoAnimeService).getEpisodeTitles(id) as T

    override suspend fun <T> fetchEpisodeMediaUrl(
        header: Map<String, String>,
        episodeUrl: String,
        extra: List<Any?>
    ): T = (animeService as GogoAnimeService).fetchEpisodeMediaUrl(header, episodeUrl) as T

    suspend fun fetchM3u8Url(
        header: Map<String, String>,
        url: String
    ) = (animeService as GogoAnimeService).fetchM3u8Url(header, url)

    suspend fun getEncryptionKeys() = (animeService as GogoAnimeService).getKeys()

    suspend fun fetchM3u8PreProcessor(
        header: Map<String, String>,
        url: String
    ) = (animeService as GogoAnimeService).fetchM3u8PreProcessor(header, url)

    suspend fun getGogoUrlFromAniListId(id: Int) =
        (animeService as GogoAnimeService).getGogoUrlFromAniListId(id)
}
