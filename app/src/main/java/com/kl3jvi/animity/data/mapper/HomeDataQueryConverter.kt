package com.kl3jvi.animity.data.mapper

import com.kl3jvi.animity.HomeDataQuery
import com.kl3jvi.animity.data.model.ui_models.*

import com.kl3jvi.animity.fragment.HomeMedia



fun HomeDataQuery.Data.convert(): HomeData {
    val trendingAnime =
        trendingAnime?.media?.mapNotNull {
            it?.homeMedia?.convert()
        } ?: listOf()
    val newAnime = newAnime?.media?.mapNotNull { it?.homeMedia?.convert() } ?: listOf()
    val movies =
        movies?.media?.mapNotNull { it?.homeMedia?.convert() } ?: listOf()
    val review = review?.reviews?.mapNotNull {
        Review(
            id = it?.id ?: 0,
            userId = it?.userId ?: 0,
            mediaId = it?.mediaId ?: 0,
            mediaType = it?.mediaType,
            summary = it?.summary ?: "",
            body = it?.body ?: "",
            rating = it ?. rating ?: 0,
            ratingAmount = it?.ratingAmount ?: 0,
            score = it?.score ?: 0,
            user = User(
                id = it?.user?.id ?: 0,
                name = it?.user?.name ?: "",
                avatar = UserAvatar(
                    it?.user?.avatar?.large ?: "",
                    it?.user?.avatar?.medium ?: ""
                )
            ),
            aniListMedia = AniListMedia(
                idAniList = it?.media?.id ?: 0,
                title = MediaTitle(romaji = it?.media?.title?.romaji ?: ""),
                bannerImage = it?.media?.bannerImage ?: "",
                coverImage = MediaCoverImage(
                    large = it?.media?.coverImage?.large ?: ""
                )
            )
        )
    } ?: listOf()

    return HomeData(
        trendingAnime = trendingAnime,
        newAnime = newAnime,
        movies = movies,
        review = review
    )
}

private fun HomeMedia?.convert(): AniListMedia {
    return AniListMedia(
        idAniList = this?.id ?: 0,
        idMal = this?.idMal,
        title = MediaTitle(romaji = this?.title?.romaji ?: ""),
        type = this?.type,
        format = this?.format,
        nextAiringEpisode = this?.nextAiringEpisode?.airingAt,
        status = this?.status,
        description = this?.description ?: "",
        startDate = if (this?.startDate?.year != null) {
            FuzzyDate(this.startDate.year, this.startDate.month, this.startDate.day)
        } else {
            null
        },
        coverImage = MediaCoverImage(
            this?.coverImage?.extraLarge ?: "",
            this?.coverImage?.large ?: "",
            this?.coverImage?.medium ?: ""
        ),
        bannerImage = this?.bannerImage ?: "",
        genres = this?.genres?.mapNotNull { Genre(name = it ?: "") } ?: listOf(),
        averageScore = this?.averageScore ?: 0,
        favourites = this?.favourites ?: 0
    )
}
