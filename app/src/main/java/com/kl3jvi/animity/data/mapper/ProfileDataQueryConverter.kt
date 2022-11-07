package com.kl3jvi.animity.data.mapper

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.AnimeListCollectionQuery
import com.kl3jvi.animity.UserQuery
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.FuzzyDate
import com.kl3jvi.animity.data.model.ui_models.Genre
import com.kl3jvi.animity.data.model.ui_models.MediaCoverImage
import com.kl3jvi.animity.data.model.ui_models.MediaTitle
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
        AniListMedia(
            idAniList = it?.media?.id ?: 0,
            title = MediaTitle(userPreferred = it?.media?.title?.userPreferred.orEmpty()),
            type = it?.media?.type,
            format = it?.media?.format,
            status = it?.media?.status,
            nextAiringEpisode = it?.media?.nextAiringEpisode?.airingAt,
            description = it?.media?.description.orEmpty(),
            startDate = if (it?.media?.startDate?.year != null) {
                FuzzyDate(
                    it.media.startDate.year,
                    it.media.startDate.month,
                    it.media.startDate.day
                )
            } else {
                null
            },
            coverImage = MediaCoverImage(
                it?.media?.coverImage?.large.orEmpty(),
                it?.media?.coverImage?.large.orEmpty(),
                it?.media?.coverImage?.large.orEmpty()
            ),
            genres = it?.media?.genres?.mapNotNull { Genre(name = it.orEmpty()) } ?: listOf(),
            averageScore = it?.media?.averageScore ?: 0
        )
    } ?: listOf()
}

data class ProfileData(
    val userData: User = User(),
    val profileRow: List<ProfileRow> = emptyList()
)

data class ProfileRow(
    val title: String = "",
    val anime: List<AniListMedia> = emptyList()
)
