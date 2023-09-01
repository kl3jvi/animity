package com.kl3jvi.animity.data.repository

import android.text.format.DateUtils
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kl3jvi.animity.data.enums.PagingDataItem
import com.kl3jvi.animity.data.enums.WeekName
import com.kl3jvi.animity.data.enums.WeekName.Companion.computeEpochTimesForDay
import com.kl3jvi.animity.data.mapper.AiringInfo
import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient
import com.kl3jvi.animity.data.paging.NotificationPagingSource
import com.kl3jvi.animity.domain.repositories.NotificationsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NotificationsRepositoryImpl @Inject constructor(
    private val aniListGraphQlClient: AniListGraphQlClient,
    private val ioDispatcher: CoroutineDispatcher,
) : NotificationsRepository {

    override fun getNotifications(): Flow<PagingData<PagingDataItem>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = true, pageSize = 20),
            pagingSourceFactory = { NotificationPagingSource(aniListGraphQlClient) },
        ).flow.flowOn(ioDispatcher)
    }

    override fun getScheduled(weekName: WeekName): Flow<List<AiringInfo>> = flow {
        val (startTime, endTime) = weekName.computeEpochTimesForDay()
        val result = aniListGraphQlClient.getAiringAnimeForDate(startTime, endTime)
        Log.e("Times", result.convert().map { time(it.airsAt) }.toString())
        emit(result.convert())
    }

    fun time(airsAt: Int?): String {
        val timeInMillis = airsAt?.times(1000L) ?: System.currentTimeMillis()
        return DateUtils.getRelativeTimeSpanString(
            timeInMillis,
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS,
        ).toString()
    }
}
