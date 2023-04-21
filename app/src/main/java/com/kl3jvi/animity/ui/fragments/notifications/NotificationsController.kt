package com.kl3jvi.animity.ui.fragments.notifications

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging3.PagingDataEpoxyController
import com.kl3jvi.animity.NotificationsBindingModel_
import com.kl3jvi.animity.data.model.ui_models.Notification

class NotificationsController : PagingDataEpoxyController<Notification>() {
    override fun buildItemModel(
        currentPosition: Int,
        item: Notification?
    ): EpoxyModel<*> {
        return NotificationsBindingModel_()
            .id(item?.id)
            .notification(item)
    }
}
