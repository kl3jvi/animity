package com.kl3jvi.animity.data.mapper

import com.kl3jvi.animity.SearchAnimeQuery
import com.kl3jvi.animity.data.model.ui_models.*

fun SearchAnimeQuery.Data.convert(): List<AniListMedia> {
    return page?.media?.mapNotNull {
        AniListMedia(
            idAniList = it?.id ?: 0,
            title = MediaTitle(userPreferred = it?.title?.userPreferred.orEmpty()),
            description = it?.description.orEmpty(),
            startDate = if (it?.startDate?.year != null) {
                FuzzyDate(
                    it.startDate.year,
                    it.startDate.month,
                    it.startDate.day
                )
            } else {
                null
            },
            coverImage = MediaCoverImage(
                it?.coverImage?.large.orEmpty(),
                it?.coverImage?.large.orEmpty(),
                it?.coverImage?.large.orEmpty()
            ),
            genres = it?.genres?.mapNotNull { genre -> Genre(name = genre.orEmpty()) } ?: emptyList(),
            averageScore = it?.averageScore ?: 0
        )
    } ?: emptyList()
}
