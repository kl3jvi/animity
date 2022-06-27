package com.kl3jvi.animity.data.repository.fragment_repositories

import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.model.ui_models.GogoAnimeKeys
import com.kl3jvi.animity.data.model.ui_models.HomeData
import com.kl3jvi.animity.data.network.anime_service.GogoAnimeApiClient
import com.kl3jvi.animity.domain.repositories.fragment_repositories.HomeRepository
import com.kl3jvi.animity.utils.NetworkResource
import com.kl3jvi.animity.utils.logError
import com.kl3jvi.animity.utils.parser.HtmlParser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Suppress("BlockingMethodInNonBlockingContext")
class HomeRepositoryImpl @Inject constructor(
    private val apiClient: GogoAnimeApiClient,
    private val ioDispatcher: CoroutineDispatcher
) : HomeRepository {

    @Inject
    override lateinit var parser: HtmlParser

    override fun getHomeData(): Flow<NetworkResource<HomeData>> {
        return try {
            apiClient.getHomeData().catch { e -> logError(e) }
                .mapNotNull {
                    var data = HomeData()
                    if (!it.hasErrors() && it.data != null) {
                        data = it.data?.convert() ?: HomeData()
                    }
                    NetworkResource.Success(data)
                }.flowOn(ioDispatcher)
        } catch (e: Exception) {
            logError(e)
            flowOf<NetworkResource.Failed<HomeData>>(
                NetworkResource.Failed(
                    e.localizedMessage ?: "Error Occurred!"
                )
            ).flowOn(ioDispatcher)
        }
    }

    override suspend fun getEncryptionKeys(): GogoAnimeKeys {
        return try {
            apiClient.getEncryptionKeys()
        } catch (e: Exception) {
            e.printStackTrace()
            GogoAnimeKeys()
        }
    }

}
