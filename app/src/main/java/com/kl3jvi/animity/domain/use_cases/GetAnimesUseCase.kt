package com.kl3jvi.animity.domain.use_cases

import com.kl3jvi.animity.data.repository.fragment_repositories.HomeRepositoryImpl
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Constants.Companion.getNetworkHeader
import com.kl3jvi.animity.utils.Resource
import com.kl3jvi.animity.utils.logError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAnimesUseCase @Inject constructor(
    private val homeRepository: HomeRepositoryImpl,
    private val ioDispatcher: CoroutineDispatcher
) {
//    operator fun invoke(): Flow<Resource<List<HomeRecyclerViewItem>>> =
//        flow<Resource<List<HomeRecyclerViewItem>>> {
//            supervisorScope {
//                try {
//                    val mutableListOfAnimeMetaModel = mutableListOf<HomeRecyclerViewItem>()
//
//                    val recentSubDubDeferred = async {
//                        homeRepository.fetchRecentSubOrDub(
//                            getNetworkHeader(),
//                            1,
//                            Constants.TYPE_RECENT_DUB
//                        )
//                    }
//                    val newSeasonDeferred =
//                        async { homeRepository.fetchNewSeason(getNetworkHeader(), 1) }
//                    val moviesDeferred =
//                        async { homeRepository.fetchMovies(getNetworkHeader(), 1) }
//                    val popularDeferred =
//                        async { homeRepository.fetchPopularFromAjax(getNetworkHeader(), 1) }
//
//                    val recentSub = recentSubDubDeferred.await()
//                    val newSeason = newSeasonDeferred.await()
//                    val movies = moviesDeferred.await()
//                    val popular = popularDeferred.await()
//
//                    mutableListOfAnimeMetaModel.add(HomeRecyclerViewItem.Title(1, "Recent Sub"))
//                    mutableListOfAnimeMetaModel.add(recentSub)
//
//                    mutableListOfAnimeMetaModel.add(HomeRecyclerViewItem.Title(2, "New Season"))
//                    mutableListOfAnimeMetaModel.add(newSeason)
//
//                    mutableListOfAnimeMetaModel.add(HomeRecyclerViewItem.Title(3, "Movies"))
//                    mutableListOfAnimeMetaModel.add(movies)
//
//                    mutableListOfAnimeMetaModel.add(HomeRecyclerViewItem.Title(4, "Popular"))
////                    mutableListOfAnimeMetaModel.addAll(popular)
//
//                    emit(Resource.Success(mutableListOfAnimeMetaModel))
//
//                } catch (e: Exception) {
//                    emit(
//                        Resource.Error<List<HomeRecyclerViewItem>>(
//                            message = e.localizedMessage ?: "An unexpected error occurred"
//                        )
//                    )
//                } catch (e: IOException) {
//                    emit(
//                        Resource.Error<List<HomeRecyclerViewItem>>(
//                            e.localizedMessage
//                                ?: "Couldn't reach server. Check your internet connection.",
//                        )
//                    )
//                }
//            }
//        }.flowOn(ioDispatcher)

    operator fun invoke() = flow {
        supervisorScope {

            try {
                val recentSubDubDeferred = async {
                    homeRepository.fetchRecentSubOrDub(
                        getNetworkHeader(),
                        1,
                        Constants.TYPE_RECENT_DUB
                    )
                }
                val newSeasonDeferred =
                    async { homeRepository.fetchNewSeason(getNetworkHeader(), 1) }
                val moviesDeferred =
                    async { homeRepository.fetchMovies(getNetworkHeader(), 1) }
                val popularDeferred =
                    async { homeRepository.fetchPopularFromAjax(getNetworkHeader(), 1) }

                val recentSub = recentSubDubDeferred.await()
                val newSeason = newSeasonDeferred.await()
                val movies = moviesDeferred.await()
                val popular = popularDeferred.await()

                val mutableList = mutableListOf(recentSub, newSeason, movies, popular)
                emit(Resource.Success(mutableList))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage))
                logError(e)
            }
        }
    }
}
