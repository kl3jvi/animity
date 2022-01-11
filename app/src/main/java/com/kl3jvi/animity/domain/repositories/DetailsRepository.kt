package com.kl3jvi.animity.domain.repositories

import com.kl3jvi.animity.data.model.AnimeInfoModel
import com.kl3jvi.animity.data.model.EpisodeModel
import com.kl3jvi.animity.data.model.EpisodeReleaseModel
import com.kl3jvi.animity.utils.parser.HtmlParser

interface DetailsRepository {
    val parser: HtmlParser
    suspend fun fetchAnimeInfo(header: Map<String, String>, episodeUrl: String): AnimeInfoModel

    suspend fun fetchEpisodeList(
        header: Map<String, String>,
        id: String,
        endEpisode: String,
        alias: String
    ): List<EpisodeModel>

    suspend fun fetchEpisodeTimeRelease(episodeUrl: String): EpisodeReleaseModel
}