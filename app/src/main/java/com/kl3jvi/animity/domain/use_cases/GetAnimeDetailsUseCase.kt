package com.kl3jvi.animity.domain.use_cases

import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import com.kl3jvi.animity.domain.repositories.fragment_repositories.DetailsRepository
import com.kl3jvi.animity.utils.Constants.Companion.getNetworkHeader
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAnimeDetailsUseCase @Inject constructor(
    private val detailsRepository: DetailsRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    /**
     * It fetches the anime info from the server and returns a flow of Resource<AnimeInfoModel>
     *
     * @param url The url of the anime you want to fetch the details of.
     */
    fun fetchAnimeInfo(url: String) = flow {
        if (url.isNotEmpty()) {
            val response = detailsRepository.fetchAnimeInfo(getNetworkHeader(), url)
            emit(
                response
            )
        }
    }.flowOn(ioDispatcher)

    /**
     * It fetches a list of episodes from the server and returns a flow of resources
     *
     * @param id The id of the show
     * @param endEpisode The episode number to start from.
     * @param alias The alias of the show.
     */
    fun fetchEpisodeList(
        id: String?,
        endEpisode: String?,
        alias: String?,
        malId: Int
    ) = flow {
        val response: List<EpisodeModel> = detailsRepository.fetchEpisodeList(
            header = getNetworkHeader(),
            id = id.orEmpty(),
            endEpisode = endEpisode.orEmpty(),
            alias = alias.orEmpty()
        ).toList().reversed()
        val episodesWithTitle =
            detailsRepository.getEpisodeTitles(malId).episodes ?: emptyList()

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
}

