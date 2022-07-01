package com.kl3jvi.animity.data.repository.fragment_repositories

import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.model.ui_models.GogoAnimeKeys
import com.kl3jvi.animity.data.model.ui_models.HomeData
import com.kl3jvi.animity.data.network.anime_service.GogoAnimeApiClient
import com.kl3jvi.animity.domain.repositories.fragment_repositories.HomeRepository
import com.kl3jvi.animity.utils.logError
import com.kl3jvi.animity.utils.parser.Parser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Suppress("BlockingMethodInNonBlockingContext")
class HomeRepositoryImpl @Inject constructor(
    private val apiClient: GogoAnimeApiClient,
    private val ioDispatcher: CoroutineDispatcher,
    override val parser: Parser
) : HomeRepository {

    override fun getHomeData(): Flow<HomeData> {
        return apiClient.getHomeData().catch { e -> logError(e) }
            .mapNotNull {
                var data = HomeData()
                if (!it.hasErrors() && it.data != null) {
                    data = it.data?.convert() ?: HomeData()
                }
                data
            }
    }


    override suspend fun getEncryptionKeys() = flow {
        try {
            emit(apiClient.getEncryptionKeys())
        } catch (e: Exception) {
            e.printStackTrace()
            emit(GogoAnimeKeys())
        }
    }

}
