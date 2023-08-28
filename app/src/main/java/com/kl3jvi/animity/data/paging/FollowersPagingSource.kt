package com.kl3jvi.animity.data.paging

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.GetFollowersListQuery
import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.model.ui_models.User
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient

class FollowersPagingSource(private val apiQlClient: AniListGraphQlClient) :
    AniListPagingSource<Pair<List<User>, List<User>>>() {

    override suspend fun fetch(page: Int): ApolloResponse<GetFollowersListQuery.Data> {
        return apiQlClient.getFollowersAndFollowing(page)
    }

    override fun convert(response: ApolloResponse<*>): List<Pair<List<User>, List<User>>> {
        val responseData = response as? ApolloResponse<GetFollowersListQuery.Data>
        return responseData?.convert() ?: emptyList()
    }
}
