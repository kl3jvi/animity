package com.kl3jvi.animity.domain.repositories

import androidx.paging.PagingData
import com.kl3jvi.animity.data.enums.PagingDataItem
import com.kl3jvi.animity.data.enums.WeekName
import com.kl3jvi.animity.data.mapper.AiringInfo
import kotlinx.coroutines.flow.Flow

interface NotificationsRepository {
    fun getNotifications(): Flow<PagingData<PagingDataItem>>
    fun getScheduled(weekName: WeekName): Flow<List<AiringInfo>>
}
