package com.kl3jvi.animity.data.repository.fragment_repositories

import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.DetailedAnimeInfo
import com.kl3jvi.animity.data.network.anime_service.AnimeApiClient
import com.kl3jvi.animity.domain.repositories.fragment_repositories.FavoriteRepository
import com.kl3jvi.animity.domain.repositories.network_repositories.NetworkBoundRepository
import com.kl3jvi.animity.utils.NetworkResource
import com.kl3jvi.animity.utils.logError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@ExperimentalCoroutinesApi
class FavoriteRepositoryImpl @Inject constructor(
    private val apiClient: AnimeApiClient,
    private val ioDispatcher: CoroutineDispatcher
) : FavoriteRepository {
    override fun getGogoUrlFromAniListId(id: Int): Flow<NetworkResource<DetailedAnimeInfo>> {
        return object : NetworkBoundRepository<DetailedAnimeInfo>() {
            override suspend fun fetchFromRemote(): Response<DetailedAnimeInfo> {
                return withContext(ioDispatcher) {
                    apiClient.getGogoUrlFromAniListId(id)
                }
            }
        }.asFlow()
    }

    override fun getFavoriteAnimesFromAniList(
        userId: Int?,
        page: Int?
    ): Flow<NetworkResource<List<AniListMedia>>> {
        return try {
            apiClient.getFavoriteAnimesFromAniList(userId, page).catch { e -> logError(e) }
                .mapNotNull {
                    var data = listOf<AniListMedia>()
                    if (!it.hasErrors() && it.data != null) {
                        data = it.data?.convert() ?: listOf()
                    }
                    NetworkResource.Success(data)
                }
        } catch (e: Exception) {
            logError(e)
            flowOf(NetworkResource.Failed(e.localizedMessage ?: "Error Occurred!"))
        }
    }
}


