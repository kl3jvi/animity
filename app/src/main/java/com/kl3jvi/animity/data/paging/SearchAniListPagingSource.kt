package com.kl3jvi.animity.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient

class SearchAniListPagingSource(
    private val apiClient: AniListGraphQlClient,
    private val query: String
) : PagingSource<Int, AniListMedia>() {

    override fun getRefreshKey(state: PagingState<Int, AniListMedia>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AniListMedia> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = apiClient.fetchSearchAniListData(query, page)
            val listOfAniListMedia = response.data?.convert() ?: emptyList()
            LoadResult.Page(
                data = listOfAniListMedia,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (listOfAniListMedia.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }
}
