package com.kl3jvi.animity.domain.repositories.fragment_repositories

import com.kl3jvi.animity.data.model.ui_models.AnimeInfoModel
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import com.kl3jvi.animity.data.model.ui_models.EpisodeReleaseModel
import com.kl3jvi.animity.data.model.ui_models.EpisodeWithTitle
import com.kl3jvi.animity.utils.parser.Parser

interface DetailsRepository {
    val parser: Parser

    suspend fun fetchAnimeInfo(header: Map<String, String>, episodeUrl: String): AnimeInfoModel

    suspend fun getEpisodeTitles(id: Int): EpisodeWithTitle

    suspend fun fetchEpisodeList(
        header: Map<String, String>,
        id: String,
        endEpisode: String,
        alias: String
    ): List<EpisodeModel>

}