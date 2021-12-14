package com.kl3jvi.animity.domain.use_cases

import com.kl3jvi.animity.model.AnimeMetaModel
import com.kl3jvi.animity.repository.HomeRepositoryImpl
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
class GetAnimesUseCase @Inject constructor(
    private val homeRepository: HomeRepositoryImpl,
    private val ioDispatcher: CoroutineDispatcher
) {

    fun fetchRecentSubOrDub(): Flow<Resource<List<AnimeMetaModel>>> = flow {
        try {
            emit(Resource.Loading())
            val response = homeRepository.fetchRecentSubOrDub(
                Constants.getHeader(),
                1,
                Constants.TYPE_RECENT_DUB
            ).toList()
            emit(
                Resource.Success(
                    data = response
                )
            )
        } catch (e: Exception) {
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

    fun fetchTodaySelectionAnime(): Flow<Resource<List<AnimeMetaModel>>> = flow {
        try {
            emit(Resource.Loading())
            val response = homeRepository.fetchPopularFromAjax(Constants.getHeader(), 1).toList()
            emit(
                Resource.Success(
                    data = response
                )
            )
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    e.localizedMessage ?: "An unexpected error occurred",
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

    fun fetchNewSeason(): Flow<Resource<List<AnimeMetaModel>>> = flow {
        try {
            emit(Resource.Loading())
            val response = homeRepository.fetchNewSeason(Constants.getHeader(), 1).toList()
            emit(
                Resource.Success(
                    data = response
                )
            )
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    e.localizedMessage ?: "An unexpected error occurred",
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

    fun fetchMovies(): Flow<Resource<List<AnimeMetaModel>>> = flow {
        try {
            emit(Resource.Loading())
            val response = homeRepository.fetchMovies(Constants.getHeader(), 1).toList()
            emit(
                Resource.Success(
                    data = response
                )
            )
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    e.localizedMessage ?: "An unexpected error occurred",
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
}
