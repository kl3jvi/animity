package com.kl3jvi.animity.domain.repositories

import androidx.paging.PagingData
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.ui.fragments.search.SortType
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun fetchAniListSearchData(
        query: String,
        sortType: List<SortType>
    ): Flow<PagingData<AniListMedia>>
}
