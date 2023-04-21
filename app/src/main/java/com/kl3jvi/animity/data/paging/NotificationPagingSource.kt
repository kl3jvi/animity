package com.kl3jvi.animity.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.model.ui_models.Notification
import com.kl3jvi.animity.data.model.ui_models.NotificationData
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient

class NotificationPagingSource(
    private val apiClient: AniListGraphQlClient
) : PagingSource<Int, Notification>() {

    override fun getRefreshKey(state: PagingState<Int, Notification>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Notification> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val notificationData =
                apiClient.getNotifications(page).data?.convert() ?: NotificationData()

            LoadResult.Page(
                data = notificationData.flatten(),
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (notificationData.airingNotifications.isEmpty() && notificationData.followingNotifications.isEmpty() && notificationData.likeNotification.isEmpty() && notificationData.messageNotifications.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    private fun NotificationData.flatten(): List<Notification> {
        // Flatten the data into a single list
        return airingNotifications + followingNotifications + likeNotification + messageNotifications
    }

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }
}
