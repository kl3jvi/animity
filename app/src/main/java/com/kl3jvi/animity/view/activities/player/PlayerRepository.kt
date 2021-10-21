package com.kl3jvi.animity.view.activities.player

import androidx.annotation.WorkerThread
import com.kl3jvi.animity.model.api.AnimeService
import javax.inject.Inject

class PlayerRepository @Inject constructor(private val apiHelper: AnimeService) {

    @WorkerThread
    suspend fun fetchEpisodeMediaUrl(header: Map<String, String>, url: String) =
        apiHelper.fetchEpisodeMediaUrl(header, url)

    @WorkerThread
    suspend fun fetchM3u8Url(header: Map<String, String>, url: String) =
        apiHelper.fetchM3u8Url(header, url)

}