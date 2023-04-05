package com.kl3jvi.animity.data.repository

import android.util.Log
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import com.kl3jvi.animity.data.model.ui_models.EpisodeWithTitle
import com.kl3jvi.animity.data.network.anime_service.base.BaseClient
import com.kl3jvi.animity.data.network.anime_service.enime.EnimeClient
import com.kl3jvi.animity.data.network.anime_service.gogo.GogoAnimeApiClient
import com.kl3jvi.animity.domain.repositories.DetailsRepository
import com.kl3jvi.animity.parsers.GoGoParser
import com.kl3jvi.animity.persistence.EpisodeDao
import com.kl3jvi.animity.settings.AnimeTypes
import com.kl3jvi.animity.settings.Settings
import com.kl3jvi.animity.settings.toStringGson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailsRepositoryImpl @Inject constructor(
    apiClient: BaseClient,
    private val ioDispatcher: CoroutineDispatcher,
    private val episodeDao: EpisodeDao,
    settings: Settings,
    override val parser: GoGoParser
) : DetailsRepository {

    private val selectedAnimeProvider = when (settings.selectedProvider) {
        AnimeTypes.GOGO_ANIME -> apiClient as GogoAnimeApiClient
        AnimeTypes.ENIME -> apiClient as EnimeClient
    }

    override fun fetchEpisodeList(
        header: Map<String, String>,
        extra: List<Any?>,
        malId: Int,
        episodeUrl: String
    ): Flow<List<EpisodeModel>> {
        return combine(
            getListOfEpisodes(episodeUrl),
            getEpisodeTitles(malId),
            getEpisodesPercentage(malId)
        ) { episodeModels, episodesWithTitle, episodeEntities ->
            episodeModels.mapIndexed { index, episodeModel ->
                if (episodeModel.getEpisodeNumberOnly() == episodesWithTitle.getOrNull(index)?.number) {
                    episodeModel.episodeName = episodesWithTitle[index].title
                    episodeModel.isFiller = episodesWithTitle[index].isFiller
                } else {
                    episodeModel.episodeName = ""
                    episodeModel.isFiller = false
                }
                episodeModel
            }.map { episode ->
                val contentEpisode =
                    episodeEntities.firstOrNull { it.episodeUrl == episode.episodeUrl }
                if (contentEpisode != null) {
                    episode.percentage = contentEpisode.getWatchedPercentage()
                }
                episode
            }
        }.flowOn(ioDispatcher)
    }

    private fun getListOfEpisodes(episodeUrl: String) = flow {
        Log.e("Selected Provider", selectedAnimeProvider.toStringGson())
        val response = selectedAnimeProvider.fetchEpisodeList<ResponseBody>(episodeUrl)
        val episodeList = parser.fetchEpisodeList(response.string()).reversed()
        emit(episodeList)
    }.catch { emit(emptyList()) }
        .flowOn(ioDispatcher)

    private fun getEpisodeTitles(id: Int) = flow {
        val response = selectedAnimeProvider.getEpisodeTitles<EpisodeWithTitle>(id).episodes
        emit(response)
    }.catch { emit(emptyList()) }.flowOn(ioDispatcher)

    private fun getEpisodesPercentage(malId: Int) = episodeDao.getEpisodesByAnime(malId = malId)
        .catch { emit(emptyList()) }
}
