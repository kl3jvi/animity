package com.kl3jvi.animity.data.repository

import com.kl3jvi.animity.data.model.ui_models.AnimeInfoModel
import com.kl3jvi.animity.data.model.ui_models.EpisodeEntity
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import com.kl3jvi.animity.data.network.anime_service.GogoAnimeApiClient
import com.kl3jvi.animity.domain.repositories.DetailsRepository
import com.kl3jvi.animity.parsers.GoGoParser
import com.kl3jvi.animity.persistence.EpisodeDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailsRepositoryImpl @Inject constructor(
    private val apiClient: GogoAnimeApiClient,
    private val ioDispatcher: CoroutineDispatcher,
    override val parser: GoGoParser,
    private val episodeDao: EpisodeDao
) : DetailsRepository {

    override fun fetchAnimeInfo(
        header: Map<String, String>,
        episodeUrl: String
    ) = flow {
        if (episodeUrl.isNotEmpty()) {
            val response = withContext(ioDispatcher) {
                parser.parseAnimeInfo(
                    apiClient.fetchAnimeInfo(header = header, episodeUrl = episodeUrl).string()
                )
            }
            emit(response)
        } else {
            emptyFlow<AnimeInfoModel>()
        }
    }.flowOn(ioDispatcher)

    override fun fetchEpisodeList(
        header: Map<String, String>,
        id: String,
        endEpisode: String,
        alias: String,
        malId: Int
    ): Flow<List<EpisodeModel>> {
        return flow {
            val response = parser.fetchEpisodeList(
                apiClient.fetchEpisodeList(
                    header = header,
                    id = id,
                    endEpisode = endEpisode,
                    alias = alias
                ).string()
            ).reversed()
            emit(response)
        }.combine(getEpisodeTitles(malId)) { episodesWithoutTitles, episodesWithTitle -> // In this combine we add title to the episodes
            episodesWithoutTitles.forEachIndexed { index, episodeModel ->
                if (episodeModel.episodeNumber.split(" ")
                        .last() == episodesWithTitle.getOrNull(index)?.number
                ) {
                    episodeModel.episodeName = episodesWithTitle[index].title
                    episodeModel.isFiller = episodesWithTitle[index].isFiller
                } else {
                    episodeModel.episodeName = ""
                }
            }
            episodesWithoutTitles
        }
            .combine(getEpisodesPercentage(malId)) { networkEpisodeList, episodeListFromDataBase -> // In this combine we add watched percentage to the episodes
                networkEpisodeList.map { episode ->
                    val contentEpisode =
                        episodeListFromDataBase.firstOrNull { it.episodeUrl == episode.episodeUrl }
                    if (contentEpisode != null) {
                        episode.percentage = contentEpisode.getWatchedPercentage()
                    }
                    episode
                }
            }.flowOn(ioDispatcher)
    }

    private fun getEpisodeTitles(id: Int) = flow {
        val response = apiClient.getEpisodeTitles(id).episodes
        emit(response)
    }.catch { emit(emptyList()) }.flowOn(ioDispatcher)

    private fun getEpisodesPercentage(malId: Int): Flow<List<EpisodeEntity>> {
        return episodeDao.getEpisodesByAnime(malId = malId)
    }
}
