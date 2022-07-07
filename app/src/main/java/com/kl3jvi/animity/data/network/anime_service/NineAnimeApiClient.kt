package com.kl3jvi.animity.data.network.anime_service

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.FavoritesAnimeQuery
import com.kl3jvi.animity.data.model.ui_models.DetailedAnimeInfo
import com.kl3jvi.animity.data.model.ui_models.GogoAnimeKeys
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NineAnimeApiClient @Inject constructor(
    private val nineAnimeService: NineAnimeService,
) : BaseClient() {
    override suspend fun getGogoUrlFromAniListId(id: Int): DetailedAnimeInfo {
        TODO("Not yet implemented")

    }

    override suspend fun getEncryptionKeys(): GogoAnimeKeys {
        TODO("Not yet implemented")
    }

}