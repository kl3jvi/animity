package com.kl3jvi.animity.repository

import com.kl3jvi.animity.network.AnimeService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailsRepository @Inject constructor(private val apiHelper: AnimeService) {

    suspend fun fetchAnimeInfo(header: Map<String, String>, episodeUrl: String) =
        apiHelper.fetchAnimeInfo(header, episodeUrl)

    suspend fun fetchEpisodeList(
        header: Map<String, String>,
        id: String,
        endEpisode: String,
        alias: String
    ) = apiHelper.fetchEpisodeList(header = header, id = id, endEpisode = endEpisode, alias = alias)

    suspend fun fetchEpisodeTimeRelease(episodeUrl: String) = apiHelper.fetchEpisodeTimeRelease(episodeUrl)
}
