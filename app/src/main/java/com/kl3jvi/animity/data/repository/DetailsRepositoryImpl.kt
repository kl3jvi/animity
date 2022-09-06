package com.kl3jvi.animity.data.repository

import com.kl3jvi.animity.data.model.ui_models.AnimeInfoModel
import com.kl3jvi.animity.data.model.ui_models.EpisodeEntity
import com.kl3jvi.animity.data.network.anime_service.GogoAnimeApiClient
import com.kl3jvi.animity.domain.repositories.DetailsRepository
import com.kl3jvi.animity.parsers.GoGoParser
import com.kl3jvi.animity.persistence.EpisodeDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
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
        } else emptyFlow<AnimeInfoModel>()
    }.flowOn(ioDispatcher)

    override fun fetchEpisodeList(
        header: Map<String, String>,
        id: String,
        endEpisode: String,
        alias: String,
        malId: Int
    ) = flow {
        val response = withContext(ioDispatcher) {
            parser.fetchEpisodeList(
                apiClient.fetchEpisodeList(
                    header = header,
                    id = id,
                    endEpisode = endEpisode,
                    alias = alias
                ).string()
            ).reversed()
        }
        emit(response)
    }.flatMapLatest { list ->
        flow {
            val episodesWithTitle = getEpisodeTitles(malId).first()
            list.forEachIndexed { index, episodeModel ->
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
            emit(list)
        }
    }

    override fun getEpisodeTitles(id: Int) = flow {
        val response = apiClient.getEpisodeTitles(id).episodes
        emit(response)
    }.catch { emit(emptyList()) }.flowOn(ioDispatcher)


    override fun getEpisodesPercentage(malId: Int): Flow<List<EpisodeEntity>> {
        return episodeDao.getEpisodesByAnime(malId = malId)
    }
}

