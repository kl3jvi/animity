package com.kl3jvi.animity.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val ioDispatcher: CoroutineDispatcher,
    private val aniListGraphQlClient: AniListGraphQlClient
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
//        val result = aniListGraphQlClient.getAiringAnimeNotifications()
//        return@withContext if (result.second) Result.failure() else
        Result.success()
    }
}
