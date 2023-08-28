package com.kl3jvi.animity.domain.repositories

import androidx.paging.PagingData
import com.kl3jvi.animity.data.enums.PagingDataItem
import kotlinx.coroutines.flow.Flow

interface NotificationsRepository {
    fun getNotifications(): Flow<PagingData<PagingDataItem>>
}
