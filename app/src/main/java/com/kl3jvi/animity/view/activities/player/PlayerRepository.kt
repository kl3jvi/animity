package com.kl3jvi.animity.view.activities.player

import androidx.annotation.WorkerThread
import com.kl3jvi.animity.model.network.ApiHelper

class PlayerRepository(private val apiHelper: ApiHelper) {

    @WorkerThread
    suspend fun fetchEpisodeMediaUrl(header: Map<String, String>, url: String) =
        apiHelper.fetchEpisodeMediaUrl(header, url)

}