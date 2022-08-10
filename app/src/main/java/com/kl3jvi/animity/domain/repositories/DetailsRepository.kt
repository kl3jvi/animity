package com.kl3jvi.animity.domain.repositories

import com.kl3jvi.animity.data.model.ui_models.*
import com.kl3jvi.animity.parsers.BaseParser
import com.kl3jvi.animity.parsers.GoGoParser
import com.kl3jvi.animity.utils.Constants.Companion.getNetworkHeader
import com.kl3jvi.animity.utils.parser.Parser
import kotlinx.coroutines.flow.Flow

interface DetailsRepository {
    val parser: GoGoParser
    fun fetchAnimeInfo(
        header: Map<String, String> = getNetworkHeader(),
        episodeUrl: String
    ): Flow<AnimeInfoModel>

    fun getEpisodeTitles(id: Int): Flow<List<Episode>>
    fun fetchEpisodeList(
        header: Map<String, String> = getNetworkHeader(),
        id: String,
        endEpisode: String,
        alias: String,
        malId: Int
    ): Flow<List<EpisodeModel>>

    fun getEpisodesPercentage(malId: Int): Flow<List<Content>>
}