package com.kl3jvi.animity.data.paging

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.SearchAnimeQuery
import com.kl3jvi.animity.data.enums.SortType
import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient

class SearchAniListPagingSource(
    private val apiClient: AniListGraphQlClient,
    private val query: String,
    private val sortType: List<SortType>,
) : AniListPagingSource<AniListMedia>() {

    override suspend fun fetch(page: Int): ApolloResponse<SearchAnimeQuery.Data> {
        return apiClient.fetchSearchAniListData(query, page, sortType.map(SortType::toMediaSort))
    }

    override fun convert(response: ApolloResponse<*>): List<AniListMedia> {
        val responseData = response as? ApolloResponse<SearchAnimeQuery.Data>
        return responseData?.data?.convert() ?: emptyList()
    }
}
