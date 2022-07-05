package com.kl3jvi.animity.data.repository.fragment_repositories

import com.kl3jvi.animity.data.model.ui_models.AnimeInfoModel
import com.kl3jvi.animity.data.network.anime_service.GogoAnimeApiClient
import com.kl3jvi.animity.domain.repositories.fragment_repositories.DetailsRepository
import com.kl3jvi.animity.utils.parser.Parser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.toList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Suppress("BlockingMethodInNonBlockingContext")
class DetailsRepositoryImpl @Inject constructor(
    private val apiClient: GogoAnimeApiClient,
    private val ioDispatcher: CoroutineDispatcher,
    override val parser: Parser
) : DetailsRepository {

    override suspend fun fetchAnimeInfo(
        header: Map<String, String>,
        episodeUrl: String
    ) = flow {
        if (episodeUrl.isNotEmpty()) {
            val response = parser.parseAnimeInfo(
                apiClient.fetchAnimeInfo(header = header, episodeUrl = episodeUrl).string()
            )
            emit(response)
        } else emptyFlow<AnimeInfoModel>()
    }.flowOn(ioDispatcher)

    override suspend fun fetchEpisodeList(
        header: Map<String, String>,
        id: String,
        endEpisode: String,
        alias: String,
        malId: Int
    ) = flow {
        val response = parser.fetchEpisodeList(
            apiClient.fetchEpisodeList(
                header = header,
                id = id,
                endEpisode = endEpisode,
                alias = alias
            ).string()
        ).toList().reversed()

        val episodesWithTitle = getEpisodeTitles(malId).toList().first().episodes ?: emptyList()

        response.forEachIndexed { index, episodeModel ->
            if (episodeModel.episodeNumber.split(" ").last() ==
                episodesWithTitle.getOrNull(
                    index
                )?.number
            ) {
                episodeModel.episodeName = episodesWithTitle[index].title
                episodeModel.isFiller = episodesWithTitle[index].isFiller
            } else {
                episodeModel.episodeName = ""
            }
        }
        emit(response)
    }.flowOn(ioDispatcher)


    override suspend fun getEpisodeTitles(id: Int) = flow {
        val response = apiClient.getEpisodeTitles(id)
        emit(response)
    }.flowOn(ioDispatcher)


}
