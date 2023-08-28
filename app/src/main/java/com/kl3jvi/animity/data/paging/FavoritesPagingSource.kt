package com.kl3jvi.animity.data.paging

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.FavoritesAnimeQuery
import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient

class FavoritesPagingSource(
    private val apiClient: AniListGraphQlClient,
    private val userId: Int?,
) : AniListPagingSource<AniListMedia>() {
    override suspend fun fetch(page: Int): ApolloResponse<FavoritesAnimeQuery.Data> {
        return apiClient.getFavoriteAnimes(userId, page)
    }

    override fun convert(response: ApolloResponse<*>): List<AniListMedia> {
        val responseData = response as ApolloResponse<FavoritesAnimeQuery.Data>
        return responseData.convert()
    }
}
