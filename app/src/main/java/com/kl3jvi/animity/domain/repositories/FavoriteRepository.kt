package com.kl3jvi.animity.domain.repositories

import androidx.paging.PagingData
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getGogoUrlFromAniListId(id: Int): Flow<String>
    fun getFavoriteAnimesFromAniList(
        userId: Int?
    ): Flow<PagingData<AniListMedia>>
}
