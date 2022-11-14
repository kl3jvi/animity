package com.kl3jvi.animity.data.network.anime_service.gogo

import javax.inject.Inject

class GogoAnimeApiClient @Inject constructor(
    private val gogoAnimeService: GogoAnimeService
) {

    suspend fun fetchAnimeInfo(
        header: Map<String, String>,
        episodeUrl: String
    ) = gogoAnimeService.fetchAnimeInfo(header, episodeUrl)

    suspend fun fetchEpisodeList(
        header: Map<String, String>,
        id: String,
        endEpisode: String,
        alias: String
    ) = gogoAnimeService.fetchEpisodeList(
        header = header,
        id = id,
        endEpisode = endEpisode,
        alias = alias
    )

    suspend fun fetchEpisodeMediaUrl(
        header: Map<String, String>,
        episodeUrl: String
    ) = gogoAnimeService.fetchEpisodeMediaUrl(header, episodeUrl)

    suspend fun fetchM3u8Url(
        header: Map<String, String>,
        url: String
    ) = gogoAnimeService.fetchM3u8Url(header, url)

    suspend fun getEncryptionKeys() = gogoAnimeService.getKeys()

    suspend fun fetchM3u8PreProcessor(
        header: Map<String, String>,
        url: String
    ) = gogoAnimeService.fetchM3u8PreProcessor(header, url)

    suspend fun getGogoUrlFromAniListId(id: Int) =
        gogoAnimeService.getGogoUrlFromAniListId(id)

    suspend fun getEpisodeTitles(id: Int) =
        gogoAnimeService.getEpisodeTitles(id)
}
