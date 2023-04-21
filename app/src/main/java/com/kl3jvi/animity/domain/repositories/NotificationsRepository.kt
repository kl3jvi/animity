package com.kl3jvi.animity.domain.repositories

import com.kl3jvi.animity.data.model.ui_models.NotificationData
import kotlinx.coroutines.flow.Flow

interface NotificationsRepository {
    fun getNotifications(): Flow<NotificationData?>
}
