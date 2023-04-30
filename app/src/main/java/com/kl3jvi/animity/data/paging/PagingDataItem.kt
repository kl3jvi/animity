package com.kl3jvi.animity.data.paging

import com.kl3jvi.animity.data.model.ui_models.Notification

sealed class PagingDataItem {
    data class NotificationItem(val notification: Notification) : PagingDataItem()
    data class HeaderItem(val header: String) : PagingDataItem()
}
