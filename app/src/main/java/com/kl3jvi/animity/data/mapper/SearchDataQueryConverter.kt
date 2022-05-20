package com.kl3jvi.animity.data.mapper

import com.kl3jvi.animity.SearchAnimeQuery
import com.kl3jvi.animity.data.model.ui_models.*


fun SearchAnimeQuery.Data.convert(): List<AniListMedia>? {
    return page?.media?.map {
        AniListMedia(
            idAniList = it?.id ?: 0,
            title = MediaTitle(userPreferred = it?.title?.userPreferred ?: ""),
            description = it?.description ?: "",
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
                it?.coverImage?.large ?: "",
                it?.coverImage?.large ?: "",
                it?.coverImage?.large ?: ""
            ),
            genres = it?.genres?.mapNotNull { Genre(name = it ?: "") } ?: listOf(),
            averageScore = it?.averageScore ?: 0,
        )
    }
}
