package com.kl3jvi.animity.data.paging

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.SearchUsersQuery
import com.kl3jvi.animity.data.model.ui_models.User
import com.kl3jvi.animity.data.model.ui_models.UserAvatar
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient

class SearchUsersPagingSource(
    private val apiClient: AniListGraphQlClient,
    private val query: String,
) : AniListPagingSource<User>() {

    override suspend fun fetch(page: Int): ApolloResponse<SearchUsersQuery.Data> {
        return apiClient.fetchUsers(query, page)
    }

    override fun convert(response: ApolloResponse<*>): List<User> {
        val responseData = response as? ApolloResponse<SearchUsersQuery.Data>

        return responseData?.data?.page?.users?.mapNotNull { userData ->
            userData?.let {
                User(
                    userData.id,
                    userData.name,
                    userData.about.orEmpty(),
                    UserAvatar(
                        userData.avatar?.large.orEmpty(),
                    ),
                )
            }
        } ?: emptyList()
    }
}
