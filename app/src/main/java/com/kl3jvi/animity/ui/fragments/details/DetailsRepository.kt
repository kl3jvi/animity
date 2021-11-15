package com.kl3jvi.animity.ui.fragments.details

import com.kl3jvi.animity.model.api.AnimeService
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
}
