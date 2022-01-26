package com.kl3jvi.animity.domain.repositories.fragment_repositories

import androidx.paging.PagingData
import com.kl3jvi.animity.data.model.AnimeMetaModel
import com.kl3jvi.animity.utils.parser.HtmlParser
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun fetchSearchData(
        header: Map<String, String>,
        keyword: String,
    ): Flow<PagingData<AnimeMetaModel>>
}