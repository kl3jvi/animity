package com.kl3jvi.animity.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.apollographql.apollo3.api.ApolloResponse

abstract class AniListPagingSource<T : Any> : PagingSource<Int, T>() {
    abstract suspend fun fetch(page: Int): ApolloResponse<*>

    abstract fun convert(response: ApolloResponse<*>): List<T>

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = fetch(page)
            val dataList = convert(response)
            LoadResult.Page(
                data = dataList,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (dataList.isEmpty()) null else page + 1,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }
}
