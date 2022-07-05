package com.kl3jvi.animity.domain.repositories.fragment_repositories

import com.kl3jvi.animity.data.model.ui_models.AnimeInfoModel
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import com.kl3jvi.animity.data.model.ui_models.EpisodeWithTitle
import com.kl3jvi.animity.utils.Constants.Companion.getNetworkHeader
import com.kl3jvi.animity.utils.parser.Parser
import kotlinx.coroutines.flow.Flow

interface DetailsRepository {
    val parser: Parser
    suspend fun fetchAnimeInfo(header: Map<String, String> = getNetworkHeader(), episodeUrl: String): Flow<AnimeInfoModel>
    suspend fun getEpisodeTitles(id: Int): Flow<EpisodeWithTitle>
    suspend fun fetchEpisodeList(
        header: Map<String, String> = getNetworkHeader(),
        id: String,
        endEpisode: String,
        alias: String,
        malId: Int
    ): Flow<List<EpisodeModel>>
}