package com.kl3jvi.animity.data.repository.fragment_repositories

import com.kl3jvi.animity.data.model.ui_models.AnimeInfoModel
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import com.kl3jvi.animity.data.model.ui_models.EpisodeWithTitle
import com.kl3jvi.animity.data.network.anime_service.GogoAnimeApiClient
import com.kl3jvi.animity.domain.repositories.fragment_repositories.DetailsRepository
import com.kl3jvi.animity.utils.parser.HtmlParser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Suppress("BlockingMethodInNonBlockingContext")
class DetailsRepositoryImpl @Inject constructor(
    private val apiClient: GogoAnimeApiClient,
    private val ioDispatcher: CoroutineDispatcher
) : DetailsRepository {

    @Inject
    override lateinit var parser: HtmlParser

    override suspend fun fetchAnimeInfo(
        header: Map<String, String>,
        episodeUrl: String
    ): AnimeInfoModel = withContext(ioDispatcher) {
        parser.parseAnimeInfo(
            apiClient.fetchAnimeInfo(header = header, episodeUrl = episodeUrl).string()
        )
    }

    override suspend fun fetchEpisodeList(
        header: Map<String, String>,
        id: String,
        endEpisode: String,
        alias: String,
    ): List<EpisodeModel> = withContext(ioDispatcher) {
        parser.fetchEpisodeList(
            apiClient.fetchEpisodeList(
                header = header,
                id = id,
                endEpisode = endEpisode,
                alias = alias
            ).string()
        )
    }

    override suspend fun getEpisodeTitles(id: Int): EpisodeWithTitle = withContext(ioDispatcher) {
        apiClient.getEpisodeTitles(
            id
        )
    }

}
