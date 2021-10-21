package com.kl3jvi.animity.domain

import com.kl3jvi.animity.model.entities.AnimeMetaModel
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Resource
import com.kl3jvi.animity.view.fragments.home.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetAnimesUseCase @Inject constructor(private val homeRepository: HomeRepository) {

    fun fetchRecentSubOrDub(): Flow<Resource<List<AnimeMetaModel>>> = flow {
        try {
            emit(Resource.Loading())
            val response =
                Constants.parseList(
                    homeRepository.fetchRecentSubOrDub(
                        Constants.getHeader(),
                        1,
                        Constants.TYPE_RECENT_DUB
                    ).string(), Constants.TYPE_RECENT_DUB
                ).toList()
            emit(
                Resource.Success(
                    data = response
                )
            )
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage ?: "An unexpected error occurred",
                    Constants.parseList(
                        homeRepository.fetchRecentSubOrDub(
                            Constants.getHeader(),
                            1,
                            Constants.TYPE_RECENT_DUB
                        ).string(), Constants.TYPE_RECENT_DUB
                    ).toList()
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    e.localizedMessage ?: "Couldn't reach server. Check your internet connection.",
                    Constants.parseList(
                        homeRepository.fetchRecentSubOrDub(
                            Constants.getHeader(),
                            1,
                            Constants.TYPE_RECENT_DUB
                        ).string(), Constants.TYPE_RECENT_DUB
                    ).toList()
                )
            )
        }
    }

    fun fetchTodaySelectionAnime(): Flow<Resource<List<AnimeMetaModel>>> = flow {
        try {
            emit(Resource.Loading())
            val response =
                Constants.parseList(
                    homeRepository.fetchPopularFromAjax(
                        Constants.getHeader(),
                        1
                    ).string(), Constants.TYPE_POPULAR_ANIME
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
                    emptyList()
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    e.localizedMessage ?: "Couldn't reach server. Check your internet connection.",
                    emptyList()
                )
            )
        }
    }

    fun fetchNewSeason(): Flow<Resource<List<AnimeMetaModel>>> = flow {
        try {
            emit(Resource.Loading())
            val response =
                Constants.parseList(
                    homeRepository.fetchNewSeason(
                        Constants.getHeader(),
                        1
                    ).string(), Constants.TYPE_NEW_SEASON
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
                    emptyList()
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    e.localizedMessage ?: "Couldn't reach server. Check your internet connection.",
                    emptyList()
                )
            )
        }
    }

    fun fetchMovies(): Flow<Resource<List<AnimeMetaModel>>> = flow {
        try {
            emit(Resource.Loading())
            val response =
                Constants.parseList(
                    homeRepository.fetchMovies(
                        Constants.getHeader(),
                        1
                    ).string(), Constants.TYPE_MOVIE
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
                    emptyList()
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    e.localizedMessage ?: "Couldn't reach server. Check your internet connection.",
                    emptyList()
                )
            )
        }
    }

}