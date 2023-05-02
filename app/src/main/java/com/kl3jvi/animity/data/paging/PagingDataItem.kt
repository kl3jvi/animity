package com.kl3jvi.animity.data.paging

import com.kl3jvi.animity.data.model.ui_models.Notification

sealed class PagingDataItem {
    data class NotificationItem(val notification: Notification) : PagingDataItem()
    data class HeaderItem(val header: String) : PagingDataItem()
}

sealed interface NotificationType {
    object Airing : NotificationType
    data class Activity(val userId: Int) : NotificationType
    object Threads : NotificationType
    object Unknown : NotificationType
    data class Following(val userId: Int) : NotificationType
}
