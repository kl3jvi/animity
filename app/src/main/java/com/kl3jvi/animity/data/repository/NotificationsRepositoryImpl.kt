package com.kl3jvi.animity.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kl3jvi.animity.data.model.ui_models.Notification
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient
import com.kl3jvi.animity.data.paging.NotificationPagingSource
import com.kl3jvi.animity.domain.repositories.NotificationsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NotificationsRepositoryImpl @Inject constructor(
    private val aniListGraphQlClient: AniListGraphQlClient,
    private val ioDispatcher: CoroutineDispatcher
) : NotificationsRepository {

    override fun getNotifications(): Flow<PagingData<Notification>> {
        val pager = Pager(
            config = PagingConfig(enablePlaceholders = true, pageSize = 20),
            pagingSourceFactory = { NotificationPagingSource(aniListGraphQlClient) }
        ).flow
        return pager.flowOn(ioDispatcher)
    }
}
