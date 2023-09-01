package com.kl3jvi.animity.data.repository

import com.kl3jvi.animity.data.enums.AnimeTypes
import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.EnimeResponse
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import com.kl3jvi.animity.data.model.ui_models.EpisodeWithTitle
import com.kl3jvi.animity.data.network.UpdateClient
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient
import com.kl3jvi.animity.data.network.anime_service.base.BaseClient
import com.kl3jvi.animity.domain.repositories.DetailsRepository
import com.kl3jvi.animity.parsers.GoGoParser
import com.kl3jvi.animity.persistence.EpisodeDao
import com.kl3jvi.animity.settings.Settings
import com.kl3jvi.animity.type.MediaListStatus
import com.kl3jvi.animity.utils.logError
import com.kl3jvi.animity.utils.providerFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import javax.inject.Inject

@OptIn(FlowPreview::class)
class DetailsRepositoryImpl @Inject constructor(
    apiClients: Map<String, @JvmSuppressWildcards BaseClient>,
    private val ioDispatcher: CoroutineDispatcher,
    private val episodeDao: EpisodeDao,
    private val settings: Settings,
    private val aniListGraphQlClient: AniListGraphQlClient,
    private val updateClient: UpdateClient,
    override val parser: GoGoParser,
) : DetailsRepository {

    private val selectedAnimeProvider: BaseClient? =
        apiClients[settings.selectedProvider.name]

    override fun fetchEpisodeList(
        header: Map<String, String>,
        extra: List<Any?>,
        malId: Int,
        episodeUrl: String,
    ): Flow<List<EpisodeModel>> {
        return combine(
            getListOfEpisodes(episodeUrl, extra),
            getEpisodesPercentage(malId),
        ) { episodeModels, episodeEntities ->

            val mediaPassed = extra.first() as? AniListMedia
            val reversedStreamingEpisode = mediaPassed?.streamingEpisode?.asReversed()
            val episodeEntitiesMap = episodeEntities.associateBy { it.episodeUrl }

            episodeModels.map { episode ->
                episode.percentage =
                    episodeEntitiesMap[episode.episodeUrl]?.getWatchedPercentage() ?: 0

                val matchingTitle = reversedStreamingEpisode
                    ?.find { streamingEpisode ->
                        val cleanedTitle = streamingEpisode.title
                            ?.replace("Episode", "EP")
                            ?.split("-")
                            ?.first()
                            ?.trim() ?: "Episode -1"
                        cleanedTitle == episode.episodeNumber
                    }?.title.orEmpty()

                episode.episodeName = matchingTitle.split("-").last().trim()
                episode
            }
        }.flowOn(ioDispatcher)
    }

    override fun changeAnimeStatus(mediaId: Int, status: MediaListStatus) = flow {
        emit(aniListGraphQlClient.markAnimeStatus(mediaId, status).convert())
    }

    private fun getListOfEpisodes(
        episodeUrl: String,
        extra: List<Any?>,
    ) = providerFlow(settings) { provider ->
        when (provider) {
            AnimeTypes.GOGO_ANIME -> {
                val response = selectedAnimeProvider?.fetchEpisodeList<ResponseBody>(episodeUrl)
                val episodeList =
                    parser.fetchEpisodeList(response?.string().orEmpty()).reversed()
                emit(episodeList)
            }

            AnimeTypes.ENIME -> {
                val response =
                    selectedAnimeProvider?.fetchEpisodeList<EnimeResponse>(episodeUrl, extra)

                val episodeList = response?.episodes?.map {
                    EpisodeModel(it.title, "Episode ${it.number}", "")
                } ?: emptyList()
                emit(episodeList)
            }
        }
    }.catch { e ->
        logError(e)
        emit(emptyList())
    }.flowOn(ioDispatcher)

    private fun getEpisodeTitles(id: Int) = flow {
        val response =
            selectedAnimeProvider?.getEpisodeTitles<EpisodeWithTitle>(id)?.episodes?.ifEmpty { emptyList() }
        emit(response)
    }.catch { emit(emptyList()) }.flowOn(ioDispatcher)

    private fun getEpisodesPercentage(malId: Int) = episodeDao.getEpisodesByAnime(malId = malId)
        .catch { emit(emptyList()) }

    override fun getUpdateVersionInfo() = flow { emit(updateClient.getUpdateInfo()) }
}
