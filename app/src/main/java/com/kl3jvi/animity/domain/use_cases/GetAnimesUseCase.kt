package com.kl3jvi.animity.domain.use_cases

import com.kl3jvi.animity.data.repository.fragment_repositories.HomeRepositoryImpl
import com.kl3jvi.animity.ui.adapters.testAdapter.HomeRecyclerViewItem
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAnimesUseCase @Inject constructor(
    private val homeRepository: HomeRepositoryImpl,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(): Flow<Resource<List<HomeRecyclerViewItem>>> = flow {
        coroutineScope {
            try {
                val mutableListOfAnimeMetaModel = mutableListOf<HomeRecyclerViewItem>()

                val recentSubDubDeferred = async {
                    homeRepository.fetchRecentSubOrDub(
                        Constants.getHeader(),
                        1,
                        Constants.TYPE_RECENT_DUB
                    )
                }
                val newSeasonDeferred =
                    async { homeRepository.fetchNewSeason(Constants.getHeader(), 1) }
                val moviesDeferred = async { homeRepository.fetchMovies(Constants.getHeader(), 1) }
                val popularDeferred =
                    async { homeRepository.fetchPopularFromAjax(Constants.getHeader(), 1) }

                val recentSub = recentSubDubDeferred.await()
                val newSeason = newSeasonDeferred.await()
                val movies = moviesDeferred.await()
                val popular = popularDeferred.await()

//                mutableListOfAnimeMetaModel.add(HomeRecyclerViewItem.Title(1, "recentSub"))
                mutableListOfAnimeMetaModel.addAll(recentSub)


//                mutableListOfAnimeMetaModel.add(HomeRecycleViewItemData("Recent Sub", recentSub))
//                mutableListOfAnimeMetaModel.add(HomeRecycleViewItemData("New Season", newSeason))
//                mutableListOfAnimeMetaModel.add(HomeRecycleViewItemData("Movies", movies))
//                mutableListOfAnimeMetaModel.add(HomeRecycleViewItemData("PopularAnimes", popular))
//
//                emit(Resource.Success(recentSub.map { it.toAnimeHorizontal() }))

            } catch (e: Exception) {
                emit(Resource.Error(message = e.localizedMessage ?: "An unexpected error occurred"))
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        e.localizedMessage
                            ?: "Couldn't reach server. Check your internet connection.",
                    )
                )
            }
        }
    }
}
