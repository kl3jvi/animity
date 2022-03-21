package com.kl3jvi.animity.data.repository.fragment_repositories

import com.kl3jvi.animity.data.model.ui_models.test.DetailedAnimeInfo
import com.kl3jvi.animity.data.network.anime_service.AnimeApiClient
import com.kl3jvi.animity.domain.repositories.fragment_repositories.FavoriteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val apiClient: AnimeApiClient,
    private val ioDispatcher: CoroutineDispatcher
) : FavoriteRepository {
    override suspend fun getGogoUrlFromAniListId(id: String): DetailedAnimeInfo =
        withContext(ioDispatcher) { apiClient.getGogoUrlFromAniListId(id) }
}