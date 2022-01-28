package com.kl3jvi.animity.domain.use_cases

import com.kl3jvi.animity.data.model.ui_models.AnimeInfoModel
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import com.kl3jvi.animity.data.model.ui_models.EpisodeReleaseModel
import com.kl3jvi.animity.data.repository.fragment_repositories.DetailsRepositoryImpl
import com.kl3jvi.animity.persistence.AnimeRepository
import com.kl3jvi.animity.persistence.EpisodeDao
import com.kl3jvi.animity.utils.Constants
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
    private val detailsRepository: DetailsRepositoryImpl,
    private val animeRepository: AnimeRepository,
    private val episodeDao: EpisodeDao,
    private val ioDispatcher: CoroutineDispatcher
) {
    fun fetchAnimeInfo(url: String): Flow<Resource<AnimeInfoModel>> = flow {
        try {
            emit(Resource.Loading())
            val response = detailsRepository.fetchAnimeInfo(Constants.getHeader(), url)
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
            val response = detailsRepository.fetchEpisodeList(
                header = Constants.getHeader(),
                id = id ?: "",
                endEpisode = endEpisode ?: "0",
                alias = alias ?: ""
            ).toList()

            // TODO bej nje listener per databazen me flow ose livedata qe te listen ndryshimet!!!

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


    fun fetchEpisodeReleaseTime(url: String): Flow<Resource<EpisodeReleaseModel>> = flow {
        try {
            emit(Resource.Loading())
            val response = detailsRepository.fetchEpisodeTimeRelease(url)
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

    fun checkIfExists(id: Int) = flow {
        emit(animeRepository.checkIfAnimeIsOnDatabase(id))
    }.flowOn(ioDispatcher)
}
