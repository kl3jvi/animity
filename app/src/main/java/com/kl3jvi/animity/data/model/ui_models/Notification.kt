package com.kl3jvi.animity.data.model.ui_models

import android.text.format.DateUtils
import com.kl3jvi.animity.data.enums.NotificationType

data class Notification(
    val type: NotificationType,
    val id: Int? = null,
    /**
     * The episode number that just aired
     */
    val episode: Int? = null,
    /**
     * The notification context text
     */
    val contexts: List<String?>? = emptyList(),
    /**
     * The user associated with the notification
     */
    val user: User? = null,
    /**
     * The associated media of the airing schedule
     */
    val media: AniListMedia = AniListMedia(),
    val createdAt: Int? = null,
) {
    fun getFormattedNotification(): String {
        return if (user != null) {
            "${user.name}${contexts?.first().orEmpty()}"
        } else {
            if (contexts?.size == 1) {
                contexts.first().orEmpty()
            } else {
                val contextList = contexts?.toMutableList() ?: mutableListOf()
                while (contextList.size < 4) contextList.add("")
                contextList.add(1, episode.toString())
                contextList.add(3, media.title.userPreferred)
                contextList.joinToString("") ?: ""
            }
        }
    }

    fun time(): String {
        val timeInMillis = createdAt?.times(1000L) ?: System.currentTimeMillis()
        return DateUtils.getRelativeTimeSpanString(
            timeInMillis,
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS,
        ).toString()
    }

    fun getNotificationImage(): String {
        return user?.avatar?.large ?: media.coverImage.large
    }
}
