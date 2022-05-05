package com.kl3jvi.animity.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.network.anime_service.AnimeApiClient
import com.kl3jvi.animity.utils.Constants.Companion.STARTING_PAGE_INDEX
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


class SearchAniListPagingSource(
    private val apiClient: AnimeApiClient,
    private val query: String
) : PagingSource<Int, AniListMedia>() {

    /**
     * > If the anchor position is not null, then get the closest page to the anchor position and
     * return the previous key
     *
     * @param state PagingState<Int, AniListMedia>
     * @return The previous key of the closest page to the anchor position.
     */
    override fun getRefreshKey(state: PagingState<Int, AniListMedia>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    /**
     * We're trying to load the data from the API, and if it's successful, we're returning a
     * `LoadResult.Page` object with the data, the previous page index, and the next page index. If it
     * fails, we're returning a `LoadResult.Error` object with the exception
     *
     * @param params LoadParams<Int> - This is the page number that we want to load.
     * @return A LoadResult object.
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AniListMedia> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            var listOfAniListMedia = listOf<AniListMedia>()
            withContext(Dispatchers.IO) {
                apiClient.fetchSearchAniListData(query, page).map { it.data?.convert() }
                    .distinctUntilChanged()
                    .collectLatest { list -> list?.let { listOfAniListMedia = it } }
            }
            LoadResult.Page(
                data = listOfAniListMedia,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (listOfAniListMedia.isNullOrEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }
}
