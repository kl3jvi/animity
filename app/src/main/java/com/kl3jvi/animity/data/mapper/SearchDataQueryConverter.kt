package com.kl3jvi.animity.data.mapper

import com.kl3jvi.animity.SearchAnimeQuery
import com.kl3jvi.animity.data.model.ui_models.*

fun SearchAnimeQuery.Data.convert(): List<AniListMedia> {
    return page?.media?.mapNotNull {
        it?.homeMedia.convert()
    } ?: emptyList()
}
