package com.kl3jvi.animity.repository

import com.kl3jvi.animity.network.AnimeService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerRepository @Inject constructor(private val apiHelper: AnimeService) {

    suspend fun fetchEpisodeMediaUrl(header: Map<String, String>, url: String) =
        apiHelper.fetchEpisodeMediaUrl(header, url)

    suspend fun fetchM3u8Url(header: Map<String, String>, url: String) =
        apiHelper.fetchM3u8Url(header, url)
}
