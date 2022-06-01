package com.kl3jvi.animity.data.mapper

import com.kl3jvi.animity.FavoritesAnimeQuery
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.MediaCoverImage
import com.kl3jvi.animity.data.model.ui_models.MediaTitle

/* It's a function that converts the data from the graphql query to the data model that we use in the
app. */
fun FavoritesAnimeQuery.Data.convert(): List<AniListMedia>? {
    return user?.favourites?.anime?.edges?.map {
        AniListMedia(
            idAniList = it?.node?.id ?: 0,
            title = MediaTitle(userPreferred = it?.node?.title?.userPreferred .orEmpty()),
            coverImage = MediaCoverImage(large = it?.node?.coverImage?.large .orEmpty()),
            description = it?.node?.description .orEmpty()
        )
    }
}