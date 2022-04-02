package com.kl3jvi.animity.data.mapper

import com.kl3jvi.animity.FavoritesAnimeQuery
import com.kl3jvi.animity.data.model.ui_models.Media
import com.kl3jvi.animity.data.model.ui_models.MediaCoverImage
import com.kl3jvi.animity.data.model.ui_models.MediaTitle

fun FavoritesAnimeQuery.Data.convert(): List<Media>? {
    return user?.favourites?.anime?.edges?.map {
        Media(
            idAniList = it?.node?.id ?: 0,
            title = MediaTitle(romaji = it?.node?.title?.romaji ?: ""),
            coverImage = MediaCoverImage(large = it?.node?.coverImage?.large ?: ""),
            description = it?.node?.description ?: ""
        )
    }
}