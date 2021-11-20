package com.kl3jvi.animity.domain

import com.kl3jvi.animity.persistence.AnimeRepository
import com.kl3jvi.animity.model.AnimeInfoModel
import com.kl3jvi.animity.model.EpisodeModel
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Resource
import com.kl3jvi.animity.utils.parser.HtmlParser
import com.kl3jvi.animity.repository.DetailsRepository
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
    private val animeRepository: AnimeRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    fun fetchAnimeInfo(url: String): Flow<Resource<AnimeInfoModel>> = flow {
        try {
            emit(Resource.Loading())
            val response =
                HtmlParser.parseAnimeInfo(
                    detailsRepository.fetchAnimeInfo(
                        Constants.getHeader(),
                        url,
                    ).string()
                )
            emit(
                Resource.Success(
                    data = response
                )
            )
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

    fun fetchEpisodeList(
        id: String?,
        endEpisode: String?,
        alias: String?
    ): Flow<Resource<List<EpisodeModel>>> = flow {
        try {
            emit(Resource.Loading())
            val response =
                HtmlParser.fetchEpisodeList(
                    detailsRepository.fetchEpisodeList(
                        header = Constants.getHeader(),
                        id = id ?: "",
                        endEpisode = endEpisode ?: "0",
                        alias = alias ?: ""
                    ).string()
                ).toList()
            emit(
                Resource.Success(
                    data = response
                )
            )
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage ?: "An unexpected error occurred"
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

    fun checkIfExists(id: Int) = flow {
        emit(animeRepository.checkIfAnimeIsOnDatabase(id))
    }.flowOn(ioDispatcher)
}
