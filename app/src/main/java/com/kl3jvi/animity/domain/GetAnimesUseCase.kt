package com.kl3jvi.animity.domain

import com.kl3jvi.animity.model.entities.AnimeMetaModel
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Resource
import com.kl3jvi.animity.ui.fragments.home.HomeRepository
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
    private val homeRepository: HomeRepository,
    private val ioDispatcher: CoroutineDispatcher
) {

    fun fetchRecentSubOrDub(): Flow<Resource<List<AnimeMetaModel>>> = flow {
        try {
            emit(Resource.Loading())
            val response =
                Constants.parseList(
                    homeRepository.fetchRecentSubOrDub(
                        Constants.getHeader(),
                        1,
                        Constants.TYPE_RECENT_DUB
                    ).string(),
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
            val response =
                Constants.parseList(
                    homeRepository.fetchPopularFromAjax(
                        Constants.getHeader(),
                        1
                    ).string(),
                    Constants.TYPE_POPULAR_ANIME
                ).toList()
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
            val response =
                Constants.parseList(
                    homeRepository.fetchNewSeason(
                        Constants.getHeader(),
                        1
                    ).string(),
                    Constants.TYPE_NEW_SEASON
                ).toList()
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
            val response =
                Constants.parseList(
                    homeRepository.fetchMovies(
                        Constants.getHeader(),
                        1
                    ).string(),
                    Constants.TYPE_MOVIE
                ).toList()
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
