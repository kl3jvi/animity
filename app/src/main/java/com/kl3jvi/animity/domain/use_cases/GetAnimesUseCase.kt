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
