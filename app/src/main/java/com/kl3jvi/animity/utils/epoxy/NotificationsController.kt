package com.kl3jvi.animity.utils.epoxy

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging3.PagingDataEpoxyController
import com.kl3jvi.animity.EmptyBindingModel_
import com.kl3jvi.animity.NotificationsBindingModel_
import com.kl3jvi.animity.TitleBindingModel_
import com.kl3jvi.animity.data.enums.NotificationType
import com.kl3jvi.animity.data.enums.PagingDataItem
import com.kl3jvi.animity.data.model.ui_models.Notification
import com.kl3jvi.animity.utils.Constants.Companion.Empty

class NotificationsController(
    private val clickListener: (Notification?, NotificationType) -> Unit,
) : PagingDataEpoxyController<PagingDataItem>() {

    private val addedTitles = mutableSetOf<String>()

    override fun buildItemModel(
        currentPosition: Int,
        item: PagingDataItem?,
    ): EpoxyModel<*> {
        return when (item) {
            is PagingDataItem.HeaderItem -> {
                if (addedTitles.add(item.header)) {
                    TitleBindingModel_()
                        .id("title_${item.header}")
                        .title(item.header)
                } else {
                    // Return an empty model to avoid adding duplicate titles
                    EmptyBindingModel_()
                        .id("empty_${item.header}")
                }
            }

            is PagingDataItem.NotificationItem -> {
                NotificationsBindingModel_()
                    .id(item.notification.id)
                    .clickListener { _ ->
                        clickListener(item.notification, item.notification.type)
                    }
                    .notification(item.notification)
            }

            null -> EmptyBindingModel_()
                .id("empty_${item?.Empty}")
        }
    }
}
