package com.kl3jvi.animity.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.model.ui_models.NotificationData
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient

class NotificationPagingSource(
    private val apiClient: AniListGraphQlClient
) : PagingSource<Int, NotificationData>() {

    override fun getRefreshKey(state: PagingState<Int, NotificationData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NotificationData> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val notificationData =
                apiClient.getNotifications(page).data?.convert() ?: NotificationData()

            LoadResult.Page(
                data = listOf(notificationData),
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (notificationData.airingNotifications.isEmpty() && notificationData.followingNotifications.isEmpty() && notificationData.likeNotification.isEmpty() && notificationData.messageNotifications.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }
}
