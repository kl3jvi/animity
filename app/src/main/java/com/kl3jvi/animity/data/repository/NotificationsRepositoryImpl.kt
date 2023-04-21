package com.kl3jvi.animity.data.repository

import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient
import com.kl3jvi.animity.domain.repositories.NotificationsRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NotificationsRepositoryImpl @Inject constructor(
    private val aniListGraphQlClient: AniListGraphQlClient
) : NotificationsRepository {
    override fun getNotifications() = flow {
        emit(aniListGraphQlClient.getNotifications().data?.convert())
    }
}
