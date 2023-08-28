package com.kl3jvi.animity.data.mapper

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.GetFollowersListQuery
import com.kl3jvi.animity.data.model.ui_models.User
import com.kl3jvi.animity.data.model.ui_models.UserAvatar

fun ApolloResponse<GetFollowersListQuery.Data>.convert(): List<Pair<List<User>, List<User>>> {
    val followers = this.data?.Page?.followers?.mapNotNull { user ->
        user?.let {
            User(
                id = it.id,
                name = it.name,
                avatar = UserAvatar(
                    large = it.avatar?.large.orEmpty(),
                ),
            )
        }
    } ?: emptyList()

    val following = this.data?.Page?.following?.mapNotNull { user ->
        user?.let {
            User(
                id = it.id,
                name = it.name,
                avatar = UserAvatar(
                    large = it.avatar?.large.orEmpty(),
                ),
            )
        }
    } ?: emptyList()

    return listOf(followers to following)
}
