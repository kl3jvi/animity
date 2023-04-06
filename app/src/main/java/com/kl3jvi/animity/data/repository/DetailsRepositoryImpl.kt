package com.kl3jvi.animity.data.repository

import android.util.Log
import com.kl3jvi.animity.data.model.ui_models.EnimeResponse
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import com.kl3jvi.animity.data.model.ui_models.EpisodeWithTitle
import com.kl3jvi.animity.data.network.anime_service.base.BaseClient
import com.kl3jvi.animity.domain.repositories.DetailsRepository
import com.kl3jvi.animity.parsers.GoGoParser
import com.kl3jvi.animity.persistence.EpisodeDao
import com.kl3jvi.animity.settings.AnimeTypes
import com.kl3jvi.animity.settings.Settings
import com.kl3jvi.animity.utils.logError
import com.kl3jvi.animity.utils.providerFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import javax.inject.Inject

class DetailsRepositoryImpl @Inject constructor(
    apiClients: Map<String, @JvmSuppressWildcards BaseClient>,
    private val ioDispatcher: CoroutineDispatcher,
    private val episodeDao: EpisodeDao,
    private val settings: Settings,
    override val parser: GoGoParser
) : DetailsRepository {

    private val selectedAnimeProvider: BaseClient? =
        apiClients[settings.selectedProvider.name] as? BaseClient

    override fun fetchEpisodeList(
        header: Map<String, String>,
        extra: List<Any?>,
        malId: Int,
        episodeUrl: String
    ): Flow<List<EpisodeModel>> {
        return combine(
            getListOfEpisodes(episodeUrl, extra),
            getEpisodeTitles(malId),
            getEpisodesPercentage(malId)
        ) { episodeModels, episodesWithTitle, episodeEntities ->
            Log.e("Episodes recieved", episodeModels.toString())
            episodeModels?.mapIndexed { index, episodeModel ->
                if (episodeModel.getEpisodeNumberOnly() == episodesWithTitle?.getOrNull(index)?.number) {
                    episodeModel.episodeName = episodesWithTitle[index].title
                    episodeModel.isFiller = episodesWithTitle[index].isFiller
                } else {
                    episodeModel.episodeName = ""
                    episodeModel.isFiller = false
                }
                episodeModel
            }?.map { episode ->
                val contentEpisode =
                    episodeEntities.firstOrNull { it.episodeUrl == episode.episodeUrl }
                if (contentEpisode != null) {
                    episode.percentage = contentEpisode.getWatchedPercentage()
                }
                episode
            }.orEmpty()
        }.flowOn(ioDispatcher)
    }

    private fun getListOfEpisodes(episodeUrl: String, extra: List<Any?>) = providerFlow(settings) {
        when (it) {
            AnimeTypes.GOGO_ANIME -> {
                val response = selectedAnimeProvider?.fetchEpisodeList<ResponseBody>(episodeUrl)
                val episodeList =
                    parser.fetchEpisodeList(response?.string().orEmpty()).reversed()
                emit(episodeList)
            }

            AnimeTypes.ENIME -> {
                val response = selectedAnimeProvider?.fetchEpisodeList<EnimeResponse>(
                    episodeUrl,
                    extra
                )
                val episodeList = response?.episodes
                emit(
                    episodeList?.map { enimeEpisode ->
                        EpisodeModel(
                            enimeEpisode.title,
                            "Episode ${enimeEpisode.number}",
                            ""
                        )
                    }
                )
            }
        }
    }.catch {
        logError(it)
        emit(emptyList())
    }.flowOn(ioDispatcher)

    private fun getEpisodeTitles(id: Int) = flow {
        val response =
            selectedAnimeProvider?.getEpisodeTitles<EpisodeWithTitle>(id)?.episodes?.ifEmpty { emptyList() }
        emit(response)
    }.catch { emit(emptyList()) }.flowOn(ioDispatcher)

    private fun getEpisodesPercentage(malId: Int) = episodeDao.getEpisodesByAnime(malId = malId)
        .catch { emit(emptyList()) }
}
