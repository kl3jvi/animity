package com.kl3jvi.animity.domain.use_cases

import com.kl3jvi.animity.data.model.ui_models.HomeData
import com.kl3jvi.animity.domain.repositories.fragment_repositories.HomeRepository
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Constants.Companion.getNetworkHeader
import com.kl3jvi.animity.utils.NetworkResource
import com.kl3jvi.animity.utils.Resource
import com.kl3jvi.animity.utils.logError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAnimesUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    operator fun invoke() = flow {
        coroutineScope {
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
                e.printStackTrace()
            }
        }
    }.flowOn(ioDispatcher)

    fun test(): Flow<NetworkResource<HomeData>> {
        return homeRepository.getHomeData()
    }
}
