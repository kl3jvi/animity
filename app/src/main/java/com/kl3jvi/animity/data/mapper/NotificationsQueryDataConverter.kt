package com.kl3jvi.animity.data.mapper

import com.kl3jvi.animity.NotificationsQuery
import com.kl3jvi.animity.data.model.ui_models.MediaTitle
import com.kl3jvi.animity.data.model.ui_models.Notification
import com.kl3jvi.animity.data.model.ui_models.NotificationMedia

fun NotificationsQuery.Data.convert(): List<Notification> {
    val data = page?.notifications?.mapNotNull {
        Notification(
            it?.onAiringNotification?.id,
            it?.onAiringNotification?.episode,
            it?.onAiringNotification?.contexts,
            it?.onAiringNotification?.media.convert()
        )
    } ?: emptyList()
    return data
}

private fun NotificationsQuery.Media?.convert(): NotificationMedia {
    return NotificationMedia(
        title = MediaTitle(userPreferred = this?.homeMedia?.title?.userPreferred.orEmpty())
    )
}
