package com.kl3jvi.animity.data.enums

import com.kl3jvi.animity.data.model.ui_models.Notification
import com.kl3jvi.animity.data.model.ui_models.User

sealed class PagingDataItem {
    data class NotificationItem(val notification: Notification) : PagingDataItem()
    data class HeaderItem(val header: String) : PagingDataItem()
}

sealed interface NotificationType {
    object Airing : NotificationType
    data class Activity(val user: User) : NotificationType
    object Threads : NotificationType
    object Unknown : NotificationType
    data class Following(val user: User) : NotificationType
}
