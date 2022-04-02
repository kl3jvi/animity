package com.kl3jvi.animity.domain.repositories.fragment_repositories

import com.kl3jvi.animity.data.model.ui_models.DetailedAnimeInfo
import com.kl3jvi.animity.data.model.ui_models.Media
import com.kl3jvi.animity.utils.NetworkResource
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getGogoUrlFromAniListId(id: Int): Flow<NetworkResource<DetailedAnimeInfo>>
    fun getFavoriteAnimesFromAniList(
        userId: Int?,
        page: Int?
    ): Flow<NetworkResource<List<Media>>>
}