package com.kl3jvi.animity.data.mapper

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.HomeDataQuery
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.Episodes
import com.kl3jvi.animity.data.model.ui_models.FuzzyDate
import com.kl3jvi.animity.data.model.ui_models.Genre
import com.kl3jvi.animity.data.model.ui_models.HomeData
import com.kl3jvi.animity.data.model.ui_models.MediaCoverImage
import com.kl3jvi.animity.data.model.ui_models.MediaTitle
import com.kl3jvi.animity.data.model.ui_models.Review
import com.kl3jvi.animity.data.model.ui_models.User
import com.kl3jvi.animity.data.model.ui_models.UserAvatar
import com.kl3jvi.animity.fragment.HomeMedia

fun ApolloResponse<HomeDataQuery.Data>.convert(): HomeData {
    return HomeData(
        trendingAnime = this.data
            ?.trendingAnime
            ?.media
            ?.mapNotNull { it?.homeMedia?.convert() }
            ?: emptyList(),
        popularAnime = this.data
            ?.popularAnime
            ?.media
            ?.mapNotNull { it?.homeMedia?.convert() }
            ?: emptyList(),
        movies = this.data
            ?.movies
            ?.media
            ?.mapNotNull { it?.homeMedia?.convert() }
            ?: emptyList(),
        review = this.data
            ?.review
            ?.reviews
            ?.mapNotNull { it.convert() }
            ?: emptyList(),
    )
}

fun HomeDataQuery.Review1?.convert(): Review {
    return Review(
        id = this?.id ?: 0,
        userId = this?.userId ?: 0,
        mediaId = this?.mediaId ?: 0,
        mediaType = this?.mediaType,
        summary = this?.summary.orEmpty(),
        body = this?.body.orEmpty(),
        rating = this?.rating ?: 0,
        ratingAmount = this?.ratingAmount ?: 0,
        score = this?.score ?: 0,
        user = User(
            id = this?.user?.id ?: 0,
            name = this?.user?.name.orEmpty(),
            avatar = UserAvatar(
                this?.user?.avatar?.large.orEmpty(),
                this?.user?.avatar?.medium.orEmpty(),
            ),
        ),
        aniListMedia = AniListMedia(
            idAniList = this?.media?.homeMedia?.id ?: 0,
            title = MediaTitle(userPreferred = this?.media?.homeMedia?.title?.userPreferred.orEmpty()),
            bannerImage = this?.media?.homeMedia?.bannerImage.orEmpty(),
            coverImage = MediaCoverImage(
                large = this?.media?.homeMedia?.coverImage?.large.orEmpty(),
            ),
        ),
    )
}

fun HomeMedia?.convert(): AniListMedia {
    return AniListMedia(
        idAniList = this?.id ?: 0,
        idMal = this?.idMal,
        title = MediaTitle(userPreferred = this?.title?.userPreferred.orEmpty()),
        type = this?.type,
        format = this?.format,
        isFavourite = this?.isFavourite ?: false,
        streamingEpisode = this?.streamingEpisodes?.mapNotNull { it.convert() },
        nextAiringEpisode = this?.nextAiringEpisode?.airingAt,
        status = this?.status,
        description = this?.description.orEmpty(),
        startDate = if (this?.startDate?.year != null) {
            FuzzyDate(this.startDate.year, this.startDate.month, this.startDate.day)
        } else {
            null
        },
        coverImage = MediaCoverImage(
            this?.coverImage?.extraLarge.orEmpty(),
            this?.coverImage?.large.orEmpty(),
            this?.coverImage?.medium.orEmpty(),
        ),
        bannerImage = this?.bannerImage.orEmpty(),
        genres = this?.genres?.mapNotNull { Genre(name = it.orEmpty()) } ?: emptyList(),
        averageScore = this?.averageScore ?: 0,
        favourites = this?.favourites ?: 0,
        mediaListEntry = MediaStatusAnimity.stringToMediaListStatus(this?.mediaListEntry?.status?.rawValue),
    )
}

fun HomeMedia.StreamingEpisode?.convert() =
    Episodes(
        this?.title,
        this?.thumbnail,
    )
