package com.kl3jvi.animity.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.data.network.anime_service.AnimeApiClient
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Constants.Companion.STARTING_PAGE_INDEX
import com.kl3jvi.animity.utils.Constants.Companion.getHeader
import com.kl3jvi.animity.utils.parser.HtmlParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchPagingSource(
    private val animeService: AnimeApiClient,
    private val query: String
) : PagingSource<Int, AnimeMetaModel>() {
    override fun getRefreshKey(state: PagingState<Int, AnimeMetaModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AnimeMetaModel> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val searchList = withContext(Dispatchers.IO) {
                HtmlParser.parseMovie(
                    animeService.fetchSearchData(
                        header = getHeader(),
                        keyword = query,
                        page = page
                    ).string(),
                    Constants.TYPE_SEARCH
                )
            }
            LoadResult.Page(
                data = searchList,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (searchList.isNullOrEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
            LoadResult.Error(exception)
        }
    }
}