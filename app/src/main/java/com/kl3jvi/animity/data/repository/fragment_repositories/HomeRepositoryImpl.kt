package com.kl3jvi.animity.data.repository.fragment_repositories

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.HomeDataQuery
import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.model.ui_models.GogoAnimeKeys
import com.kl3jvi.animity.data.model.ui_models.HomeData
import com.kl3jvi.animity.data.network.anilist_service.AniListClient
import com.kl3jvi.animity.data.network.anime_service.GogoAnimeApiClient
import com.kl3jvi.animity.domain.repositories.fragment_repositories.HomeRepository
import com.kl3jvi.animity.utils.GoGoAnime
import com.kl3jvi.animity.utils.logError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepositoryImpl @Inject constructor(
    private val aniListClient: AniListClient,
    @GoGoAnime private val animeClient: GogoAnimeApiClient,
    private val ioDispatcher: CoroutineDispatcher,
) : HomeRepository {

    override fun getHomeData(): Flow<HomeData> {
        return aniListClient.getHomeData()
            .catch { e -> logError(e) }
            .mapNotNull(ApolloResponse<HomeDataQuery.Data>::convert)
            .flowOn(ioDispatcher)
    }


    override fun getEncryptionKeys() = flow {
        try {
            emit(animeClient.getEncryptionKeys())
        } catch (e: Exception) {
            e.printStackTrace()
            emit(GogoAnimeKeys())
        }
    }.flowOn(ioDispatcher)

}
