package com.kl3jvi.animity.data.mapper

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.FavoritesAnimeQuery
import com.kl3jvi.animity.data.model.ui_models.AniListMedia

/* It's a function that converts the data from the graphql query to the data model that we use in the
app. */
fun ApolloResponse<FavoritesAnimeQuery.Data>.convert(): List<AniListMedia> {
    return this.data?.user?.favourites?.anime?.edges?.mapNotNull { it?.node?.homeMedia.convert() }
        ?: emptyList()
}

