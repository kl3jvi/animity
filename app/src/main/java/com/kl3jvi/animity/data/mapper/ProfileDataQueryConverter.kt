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
    return data?.user?.let {
        User(
            it.id,
            it.name,
            it.about.orEmpty(),
            UserAvatar(
                it.avatar?.large.orEmpty(),
                it.avatar?.medium.orEmpty()
            ),
            it.bannerImage.orEmpty()
        )
    } ?: User()
}

fun ApolloResponse<AnimeListCollectionQuery.Data>.convert(): List<ProfileRow> {
    val data = this.data
    return data?.media?.lists?.mapNotNull {
        it?.let {
            ProfileRow(
                it.name.orEmpty(),
                it.entries.convert()
            )
        }
    } ?: emptyList()
}


private fun List<AnimeListCollectionQuery.Entry?>?.convert(): List<AniListMedia> {
    return this?.mapNotNull { it?.media?.homeMedia.convert() } ?: emptyList()
}

