package com.kl3jvi.animity.repository

import com.kl3jvi.animity.domain.repositories.DetailsRepository
import com.kl3jvi.animity.model.AnimeInfoModel
import com.kl3jvi.animity.model.EpisodeModel
import com.kl3jvi.animity.model.EpisodeReleaseModel
import com.kl3jvi.animity.network.AnimeApiClient
import com.kl3jvi.animity.utils.parser.HtmlParser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailsRepositoryImpl @Inject constructor(
    private val apiClient: AnimeApiClient
) : DetailsRepository {
    override val parser: HtmlParser
        get() = HtmlParser

    override suspend fun fetchAnimeInfo(
        header: Map<String, String>,
        episodeUrl: String
    ): AnimeInfoModel {
        return parser.parseAnimeInfo(apiClient.fetchAnimeInfo(header, episodeUrl).string())
    }

    override suspend fun fetchEpisodeList(
        header: Map<String, String>,
        id: String,
        endEpisode: String,
        alias: String
    ): ArrayList<EpisodeModel> {
        return parser.fetchEpisodeList(
            apiClient.fetchEpisodeList(
                header = header,
                id = id,
                endEpisode = endEpisode,
                alias = alias
            ).string()
        )
    }

    override suspend fun fetchEpisodeTimeRelease(episodeUrl: String): EpisodeReleaseModel =
        parser.fetchEpisodeReleaseTime(apiClient.fetchEpisodeTimeRelease(episodeUrl).string())

}
