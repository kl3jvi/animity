package com.kl3jvi.animity.data.network.anime_service.gogo

import com.kl3jvi.animity.data.model.ui_models.Keys
import com.kl3jvi.animity.data.network.anime_service.base.ApiServiceSingleton
import com.kl3jvi.animity.data.network.anime_service.base.BaseClient
import com.kl3jvi.animity.data.network.anime_service.base.BaseService
import com.kl3jvi.animity.parsers.GoGoParser
import com.kl3jvi.animity.utils.Constants
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Suppress("UNCHECKED_CAST")
class GogoAnimeApiClient
@Inject
constructor(
    apiServiceSingleton: ApiServiceSingleton,
    override val parser: GoGoParser,
) : BaseClient {
    override var animeService: BaseService =
        apiServiceSingleton.run {
            updateBaseUrl(Constants.GOGO_BASE_URL)
            getApiService(GogoAnimeService::class.java)
        }

    private suspend fun fetchAnimeInfo(
        header: Map<String, String>,
        episodeUrl: String,
    ) = withContext(Dispatchers.IO) {
        (animeService as GogoAnimeService).fetchAnimeInfo(
            header,
            episodeUrl,
        ).string()
    }

    override suspend fun <T> fetchEpisodeList(
        episodeUrl: String,
        extra: List<Any?>,
    ): T {
        val animeInfo =
            fetchAnimeInfo(
                Constants.getNetworkHeader(),
                episodeUrl = episodeUrl,
            ).run(parser::parseAnimeInfo)

        return (animeService as GogoAnimeService).fetchEpisodeList(
            header = Constants.getNetworkHeader(),
            id = animeInfo.id,
            endEpisode = animeInfo.endEpisode,
            alias = animeInfo.alias,
        ) as T
    }

    override suspend fun <T> getEpisodeTitles(id: Int): T = Unit as T

    override suspend fun <T> fetchEpisodeMediaUrl(
        header: Map<String, String>,
        episodeUrl: String,
        extra: List<Any?>,
    ): T {
        val urls = mutableListOf<List<String>>()
        // fetch the current episode
        urls.add(getParsedUrls(header, episodeUrl))

        // fetch and parse the current episode to get nextEpisodeUrl
        val episodeInfo =
            parser.parseMediaUrl(
                (animeService as GogoAnimeService).fetchEpisodeMediaUrl(
                    header,
                    episodeUrl,
                ).string(),
            )

        // fetch the next episode if exists
        if (episodeInfo.nextEpisodeUrl != null) {
            urls.add(getParsedUrls(header, episodeInfo.nextEpisodeUrl.orEmpty()))
        }

        return urls as T
    }

    private suspend fun getParsedUrls(
        header: Map<String, String>,
        episodeUrl: String,
    ): List<String> {
        val episodeInfo =
            parser.parseMediaUrl(
                (animeService as GogoAnimeService).fetchEpisodeMediaUrl(
                    header,
                    episodeUrl,
                ).string(),
            )

        val id =
            Regex("id=([^&]+)").find(episodeInfo.vidCdnUrl.orEmpty())?.value?.removePrefix("id=")

        val ajaxResponse =
            parser.parseEncryptAjax(
                response =
                fetchM3u8Url(
                    header = header,
                    url = episodeInfo.vidCdnUrl.orEmpty(),
                ).string(),
                id = id.orEmpty(),
            )

        val streamUrl = "${Constants.REFERER}encrypt-ajax.php?$ajaxResponse"

        return parser.parseEncryptedUrls(
            fetchM3u8PreProcessor(
                header = header,
                url = streamUrl,
            ).string(),
        )
    }

    private suspend fun fetchM3u8Url(
        header: Map<String, String>,
        url: String,
    ) = (animeService as GogoAnimeService).fetchM3u8Url(header, url)

    fun getEncryptionKeys() = keysAndIv

    private suspend fun fetchM3u8PreProcessor(
        header: Map<String, String>,
        url: String,
    ) = (animeService as GogoAnimeService).fetchM3u8PreProcessor(header, url)

    suspend fun getGogoUrlFromAniListId(id: Int) = (animeService as GogoAnimeService).getGoGoDetailedUrl(id)

    companion object {
        val keysAndIv: Keys =
            Keys(
                key = "37911490979715163134003223491201",
                secondKey = "54674138327930866480207815084989",
                iv = "3134003223491201",
            )
    }
}
