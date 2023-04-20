package com.kl3jvi.animity.data.mapper

import com.kl3jvi.animity.NotificationsQuery
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.FuzzyDate
import com.kl3jvi.animity.data.model.ui_models.Genre
import com.kl3jvi.animity.data.model.ui_models.MediaCoverImage
import com.kl3jvi.animity.data.model.ui_models.MediaTitle
import com.kl3jvi.animity.data.model.ui_models.Notification

fun NotificationsQuery.Data.convert(): List<Notification> {
    val data = page?.notifications?.mapNotNull {
        Notification(
            it?.onAiringNotification?.id,
            it?.onAiringNotification?.episode,
            it?.onAiringNotification?.contexts,
            it?.onAiringNotification?.media.convert()
        )
    } ?: emptyList()
    return data
}

private fun NotificationsQuery.Media?.convert(): AniListMedia {
    return AniListMedia(
        idAniList = this?.homeMedia?.id ?: 0,
        idMal = this?.homeMedia?.idMal,
        title = MediaTitle(userPreferred = this?.homeMedia?.title?.userPreferred.orEmpty()),
        type = this?.homeMedia?.type,
        format = this?.homeMedia?.format,
        isFavourite = this?.homeMedia?.isFavourite ?: false,
        streamingEpisode = this?.homeMedia?.streamingEpisodes?.mapNotNull { it.convert() },
        nextAiringEpisode = this?.homeMedia?.nextAiringEpisode?.airingAt,
        status = this?.homeMedia?.status,
        description = this?.homeMedia?.description.orEmpty(),
        startDate = if (this?.homeMedia?.startDate?.year != null) {
            FuzzyDate(this.homeMedia.startDate.year, this.homeMedia.startDate.month, this.homeMedia.startDate.day)
        } else {
            null
        },
        coverImage = MediaCoverImage(
            this?.homeMedia?.coverImage?.extraLarge.orEmpty(),
            this?.homeMedia?.coverImage?.large.orEmpty(),
            this?.homeMedia?.coverImage?.medium.orEmpty()
        ),
        bannerImage = this?.homeMedia?.bannerImage.orEmpty(),
        genres = this?.homeMedia?.genres?.mapNotNull { Genre(name = it.orEmpty()) } ?: emptyList(),
        averageScore = this?.homeMedia?.averageScore ?: 0,
        favourites = this?.homeMedia?.favourites ?: 0
    )
}
