package com.kl3jvi.animity.data.paging

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.NotificationsQuery
import com.kl3jvi.animity.data.enums.PagingDataItem
import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient

class NotificationPagingSource(
    private val apiClient: AniListGraphQlClient,
) : AniListPagingSource<PagingDataItem>() {

    override suspend fun fetch(page: Int): ApolloResponse<NotificationsQuery.Data> {
        return apiClient.getNotifications(page)
    }

    override fun convert(response: ApolloResponse<*>): List<PagingDataItem> {
        val responseData = response as? ApolloResponse<NotificationsQuery.Data>
        return responseData?.data?.convert() ?: emptyList()
    }
}
