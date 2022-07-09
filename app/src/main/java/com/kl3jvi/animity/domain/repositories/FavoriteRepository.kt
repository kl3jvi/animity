package com.kl3jvi.animity.domain.repositories

import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.DetailedAnimeInfo
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getGogoUrlFromAniListId(id: Int): Flow<DetailedAnimeInfo>
    fun getFavoriteAnimesFromAniList(
        userId: Int?,
        page: Int?
    ): Flow<List<AniListMedia>>
}