package com.kl3jvi.animity.domain.use_cases

import com.kl3jvi.animity.data.model.ui_models.AnimeInfoModel
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import com.kl3jvi.animity.data.model.ui_models.EpisodeReleaseModel
import com.kl3jvi.animity.domain.repositories.fragment_repositories.DetailsRepository
import com.kl3jvi.animity.utils.Constants.Companion.getNetworkHeader
import com.kl3jvi.animity.utils.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
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
    fun fetchAnimeInfo(url: String): Flow<Resource<AnimeInfoModel>> = flow {
        emit(Resource.Loading())
        try {
            if (url.isNotEmpty()) {
                val response = detailsRepository.fetchAnimeInfo(getNetworkHeader(), url)
                emit(
                    Resource.Success(
                        data = response
                    )
                )
            }
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage ?: "An unexpected error occurred",
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    e.localizedMessage ?: "Couldn't reach server. Check your internet connection.",
                )
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
    ): Flow<Resource<List<EpisodeModel>>> = flow {
        emit(Resource.Loading())
        var response = emptyList<EpisodeModel>()
        return@flow try {
            response = detailsRepository.fetchEpisodeList(
                header = getNetworkHeader(),
                id = id .orEmpty(),
                endEpisode = endEpisode ?: "0",
                alias = alias .orEmpty()
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


            //            val episodeWithTitlesList = episodesWithTitle.associateBy({ it.number }, { it.title })
            //
            //            val episodesWithoutTitlesList =
            //                response.associateBy({ it.episodeNumber.split(" ").last() }, { it })
            //
            ////                val listEpisodes = listWithEpisodeWithTitles.entries.mapNotNull {
            ////                    episodesWithoutTitles[it.key]?.episodeName = it.value
            ////                    episodesWithoutTitles[it.key]
            ////                }

            emit(
                Resource.Success(
                    data = response
                )
            )


        } catch (e: HttpException) {
            emit(
                Resource.Success(
                    data = response
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    e.localizedMessage ?: "Couldn't reach server. Check your internet connection."
                )
            )
        }
    }.flowOn(ioDispatcher)

}

