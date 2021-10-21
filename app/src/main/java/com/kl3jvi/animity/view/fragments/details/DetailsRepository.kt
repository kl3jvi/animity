package com.kl3jvi.animity.view.fragments.details

import androidx.annotation.WorkerThread
import com.kl3jvi.animity.model.api.AnimeService
import javax.inject.Inject

class DetailsRepository @Inject constructor(private val apiHelper: AnimeService) {

    @WorkerThread
    suspend fun fetchAnimeInfo(header: Map<String, String>, episodeUrl: String) =
        apiHelper.fetchAnimeInfo(header, episodeUrl)

    @WorkerThread
    suspend fun fetchEpisodeList(
        header: Map<String, String>,
        id: String,
        endEpisode: String,
        alias: String
    ) = apiHelper.fetchEpisodeList(header=header, id = id, endEpisode = endEpisode, alias = alias)
}