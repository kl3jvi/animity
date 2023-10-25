package com.kl3jvi.animity.data.repository

import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient
import com.kl3jvi.animity.data.network.anime_service.gogo.GogoAnimeApiClient
import com.kl3jvi.animity.domain.repositories.HomeRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

@Singleton
class HomeRepositoryImpl
@Inject
constructor(
    private val aniListGraphQlClient: AniListGraphQlClient,
    private val animeClient: GogoAnimeApiClient,
    private val ioDispatcher: CoroutineDispatcher,
) : HomeRepository {
    override fun getHomeData() =
        flow {
            emit(aniListGraphQlClient.getHomeData().convert())
        }.flowOn(ioDispatcher)

    override fun getEncryptionKeys() =
        flow {
            emit(animeClient.getEncryptionKeys())
        }.flowOn(ioDispatcher)
}
