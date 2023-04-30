package com.kl3jvi.animity.ui.fragments.notifications

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging3.PagingDataEpoxyController
import com.kl3jvi.animity.NotificationsBindingModel_
import com.kl3jvi.animity.TitleBindingModel_
import com.kl3jvi.animity.data.model.ui_models.Notification
import com.kl3jvi.animity.data.paging.PagingDataItem

class NotificationsController(
    private val clickListener: (Notification?) -> Unit
) : PagingDataEpoxyController<PagingDataItem>() {
    override fun buildItemModel(
        currentPosition: Int,
        item: PagingDataItem?
    ): EpoxyModel<*> {
        return when (item) {
            is PagingDataItem.HeaderItem -> {
                TitleBindingModel_()
                    .id("title_${item.header}")
                    .title(item.header)
            }

            is PagingDataItem.NotificationItem -> {
                NotificationsBindingModel_()
                    .id(item.notification.id)
                    .clickListener { _ ->
                        clickListener(item.notification)
                    }
                    .notification(item.notification)
            }

            null -> throw IllegalStateException("Data should not be null")
        }
    }
}
