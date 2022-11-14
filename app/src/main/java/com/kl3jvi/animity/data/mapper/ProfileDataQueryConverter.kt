package com.kl3jvi.animity.data.mapper

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.AnimeListCollectionQuery
import com.kl3jvi.animity.UserQuery
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.ProfileRow
import com.kl3jvi.animity.data.model.ui_models.User
import com.kl3jvi.animity.data.model.ui_models.UserAvatar

fun ApolloResponse<UserQuery.Data>.convert(): User {
    val data = this.data
    var user = User()
    if (data?.user != null) {
        user = User(
            data.user.id,
            data.user.name,
            data.user.about.orEmpty(),
            UserAvatar(
                data.user.avatar?.large.orEmpty(),
                data.user.avatar?.medium.orEmpty()
            ),
            data.user.bannerImage.orEmpty()
        )
    }
    return user
}

fun ApolloResponse<AnimeListCollectionQuery.Data>.convert(): List<ProfileRow> {
    val data = this.data
    val list: List<ProfileRow> = data?.media?.lists?.mapNotNull {
        ProfileRow(
            it?.name.orEmpty(),
            it?.entries.convert()
        )
    } ?: emptyList()
    return list
}

private fun List<AnimeListCollectionQuery.Entry?>?.convert(): List<AniListMedia> {
    return this?.mapNotNull {
        it?.media?.homeMedia.convert()
    } ?: listOf()
}
