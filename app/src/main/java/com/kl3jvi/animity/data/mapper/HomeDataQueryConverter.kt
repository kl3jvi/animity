package com.kl3jvi.animity.data.mapper

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.HomeDataQuery
import com.kl3jvi.animity.data.model.ui_models.*
import com.kl3jvi.animity.fragment.HomeMedia

fun ApolloResponse<HomeDataQuery.Data>.convert(): HomeData {
    val data = this.data
    var homeData = HomeData()
    if (!this.hasErrors() && this.data != null) {
        val trendingAnime =
            data?.trendingAnime?.media?.mapNotNull {
                it?.homeMedia?.convert()
            } ?: emptyList()
        val popularAnime =
            data?.popularAnime?.media?.mapNotNull { it?.homeMedia?.convert() } ?: emptyList()
        val movies =
            data?.movies?.media?.mapNotNull { it?.homeMedia?.convert() } ?: emptyList()
        val review = data?.review?.reviews?.mapNotNull {
            it.convert()
        } ?: emptyList()

        homeData = HomeData(
            trendingAnime = trendingAnime,
            popularAnime = popularAnime,
            movies = movies,
            review = review
        )
    }

    return homeData
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
                this?.user?.avatar?.medium.orEmpty()
            )
        ),
        aniListMedia = AniListMedia(
            idAniList = this?.media?.id ?: 0,
            title = MediaTitle(userPreferred = this?.media?.title?.userPreferred.orEmpty()),
            bannerImage = this?.media?.bannerImage.orEmpty(),
            coverImage = MediaCoverImage(
                large = this?.media?.coverImage?.large.orEmpty()
            )
        )
    )
}

fun HomeMedia?.convert(): AniListMedia {
    return AniListMedia(
        idAniList = this?.id ?: 0,
        idMal = this?.idMal,
        title = MediaTitle(userPreferred = this?.title?.userPreferred.orEmpty()),
        type = this?.type,
        format = this?.format,
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
            this?.coverImage?.medium.orEmpty()
        ),
        bannerImage = this?.bannerImage.orEmpty(),
        genres = this?.genres?.mapNotNull { Genre(name = it.orEmpty()) } ?: emptyList(),
        averageScore = this?.averageScore ?: 0,
        favourites = this?.favourites ?: 0
    )
}

fun HomeMedia.StreamingEpisode?.convert() =
    Episodes(
        this?.title,
        this?.thumbnail
    )
