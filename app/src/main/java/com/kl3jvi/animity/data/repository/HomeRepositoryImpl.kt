package com.kl3jvi.animity.data.repository

import androidx.lifecycle.asFlow
import androidx.work.*
import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.HomeDataQuery
import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient
import com.kl3jvi.animity.data.network.anime_service.gogo.GogoAnimeApiClient
import com.kl3jvi.animity.domain.repositories.HomeRepository
import com.kl3jvi.animity.workers.NotificationWorker
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepositoryImpl @Inject constructor(
    private val aniListGraphQlClient: AniListGraphQlClient,
    private val animeClient: GogoAnimeApiClient,
    private val ioDispatcher: CoroutineDispatcher,
    private val workManager: WorkManager
) : HomeRepository {

    override lateinit var work: PeriodicWorkRequest

    init {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(false)
            .build()

        work = PeriodicWorkRequestBuilder<NotificationWorker>(10, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

//        workManager.enqueue(work)
    }

    override fun getHomeData() =
        aniListGraphQlClient.getHomeData()
            .mapNotNull(ApolloResponse<HomeDataQuery.Data>::convert)
            .flowOn(ioDispatcher)

    override fun getEncryptionKeys() = flow {
        emit(animeClient.getEncryptionKeys())
    }.flowOn(ioDispatcher)

    override fun getNotifications(): Flow<WorkInfo> {
        return workManager.getWorkInfoByIdLiveData(work.id).asFlow()
    }
}
