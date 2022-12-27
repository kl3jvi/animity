package com.kl3jvi.animity.data.mapper

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.AnimeListCollectionQuery
import com.kl3jvi.animity.UserQuery
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.ProfileRow
import com.kl3jvi.animity.data.model.ui_models.User
import com.kl3jvi.animity.data.model.ui_models.UserAvatar

fun ApolloResponse<UserQuery.Data>.convert(): User {
    val userData = this.data?.user ?: return User()
    return User(
        userData.id,
        userData.name,
        userData.about.orEmpty(),
        UserAvatar(
            userData.avatar?.large.orEmpty(),
            userData.avatar?.medium.orEmpty()
        ),
        userData.bannerImage.orEmpty()
    )
}

fun ApolloResponse<AnimeListCollectionQuery.Data>.convert(): List<ProfileRow> {
    return this.data?.media?.lists?.mapNotNull {
        ProfileRow(
            it?.name.orEmpty(),
            it?.entries.convert()
        )
    } ?: emptyList()
}


private fun List<AnimeListCollectionQuery.Entry?>?.convert(): List<AniListMedia> {
    return this?.mapNotNull {
        it?.media?.homeMedia.convert()
    } ?: emptyList()
}
