package com.kl3jvi.animity.data.mapper

import com.kl3jvi.animity.FavoritesAnimeQuery
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.MediaCoverImage
import com.kl3jvi.animity.data.model.ui_models.MediaTitle

fun FavoritesAnimeQuery.Data.convert(): List<AniListMedia>? {
    return user?.favourites?.anime?.edges?.map {
        AniListMedia(
            idAniList = it?.node?.id ?: 0,
            title = MediaTitle(romaji = it?.node?.title?.romaji ?: ""),
            coverImage = MediaCoverImage(large = it?.node?.coverImage?.large ?: ""),
            description = it?.node?.description ?: ""
        )
    }
}