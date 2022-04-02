package com.kl3jvi.animity.data.repository.fragment_repositories

import com.kl3jvi.animity.data.model.ui_models.DetailedAnimeInfo
import com.kl3jvi.animity.data.network.anime_service.AnimeApiClient
import com.kl3jvi.animity.domain.repositories.fragment_repositories.FavoriteRepository
import com.kl3jvi.animity.domain.repositories.network_repositories.NetworkBoundRepository
import com.kl3jvi.animity.utils.NetworkResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
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
}