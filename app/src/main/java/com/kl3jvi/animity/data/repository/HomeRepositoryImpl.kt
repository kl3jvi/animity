package com.kl3jvi.animity.data.repository

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.HomeDataQuery
import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient
import com.kl3jvi.animity.data.network.anime_service.GogoAnimeApiClient
import com.kl3jvi.animity.domain.repositories.HomeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepositoryImpl @Inject constructor(
    private val aniListGraphQlClient: AniListGraphQlClient,
    private val animeClient: GogoAnimeApiClient,
    private val ioDispatcher: CoroutineDispatcher
) : HomeRepository {

    override fun getHomeData() = aniListGraphQlClient.getHomeData()
        .mapNotNull(ApolloResponse<HomeDataQuery.Data>::convert)
        .flowOn(ioDispatcher)

    override fun getEncryptionKeys() = flow {
        emit(animeClient.getEncryptionKeys())
    }.flowOn(ioDispatcher)
}
