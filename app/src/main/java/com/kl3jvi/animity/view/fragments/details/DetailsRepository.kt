package com.kl3jvi.animity.view.fragments.details

import androidx.annotation.WorkerThread
import com.kl3jvi.animity.model.network.ApiHelper

class DetailsRepository(private val apiHelper: ApiHelper) {

    @WorkerThread
    suspend fun fetchAnimeInfo(header: Map<String, String>, episodeUrl: String) =
        apiHelper.fetchAnimeInfo(header, episodeUrl)

    @WorkerThread
    suspend fun fetchEpisodeList(
        header: Map<String, String>,
        id: String,
        endEpisode: String,
        alias: String
    ) = apiHelper.fetchEpisodeList(header, id, endEpisode, alias)
}