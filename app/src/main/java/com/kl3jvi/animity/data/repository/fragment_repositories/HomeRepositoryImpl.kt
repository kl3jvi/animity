package com.kl3jvi.animity.data.repository.fragment_repositories

import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.data.model.ui_models.HomeData
import com.kl3jvi.animity.data.network.anime_service.AnimeApiClient
import com.kl3jvi.animity.domain.repositories.fragment_repositories.HomeRepository
import com.kl3jvi.animity.utils.Constants.Companion.TYPE_MOVIE
import com.kl3jvi.animity.utils.Constants.Companion.TYPE_NEW_SEASON
import com.kl3jvi.animity.utils.Constants.Companion.TYPE_POPULAR_ANIME
import com.kl3jvi.animity.utils.Constants.Companion.TYPE_RECENT_SUB
import com.kl3jvi.animity.utils.NetworkResource
import com.kl3jvi.animity.utils.logError
import com.kl3jvi.animity.utils.parser.HtmlParser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Suppress("BlockingMethodInNonBlockingContext")
class HomeRepositoryImpl @Inject constructor(
    private val apiClient: AnimeApiClient,

    private val ioDispatcher: CoroutineDispatcher
) : HomeRepository {
    override val parser: HtmlParser
        get() = HtmlParser

    override suspend fun fetchRecentSubOrDub(
        header: Map<String, String>,
        page: Int,
        type: Int
    ): List<AnimeMetaModel> = withContext(ioDispatcher) {
        parser.parseRecentSubOrDub(
            apiClient.fetchRecentSubOrDub(header = header, page = page, type = type).string(),
            TYPE_RECENT_SUB
        )
    }

    override suspend fun fetchPopularFromAjax(
        header: Map<String, String>,
        page: Int
    ): List<AnimeMetaModel> = withContext(ioDispatcher) {
        parser.parsePopular(
            apiClient.fetchPopularFromAjax(header = header, page = page).string(),
            TYPE_POPULAR_ANIME
        )
    }

    override suspend fun fetchNewSeason(
        header: Map<String, String>,
        page: Int
    ): List<AnimeMetaModel> = withContext(ioDispatcher) {
        parser.parseMovie(
            apiClient.fetchNewSeason(header = header, page = page).string(),
            TYPE_NEW_SEASON
        )
    }

    override suspend fun fetchMovies(
        header: Map<String, String>,
        page: Int
    ): List<AnimeMetaModel> = withContext(ioDispatcher) {
        parser.parseMovie(
            apiClient.fetchMovies(header = header, page = page).string(),
            TYPE_MOVIE
        )
    }

    override fun getHomeData(): Flow<NetworkResource<HomeData>> {
        return try {
            apiClient.getHomeData().catch { e -> logError(e) }
                .mapNotNull {
                    val data = it.data?.convert() ?: HomeData()
                    NetworkResource.Success(data)
                }
        } catch (e: Exception) {
            logError(e)
            flowOf(NetworkResource.Failed(e.localizedMessage ?: "Error Occurred!"))
        }
    }


}
