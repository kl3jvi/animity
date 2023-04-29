package com.kl3jvi.animity.data.model.ui_models

data class Notification(
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
    val media: AniListMedia = AniListMedia()
) {
    fun getFormattedNotification(): String {
        return if (user != null) {
            "${user.name}${contexts?.first().orEmpty()}"
        } else {
            if (contexts?.size == 1) contexts.first().orEmpty()
            else {
                val contextList = contexts?.toMutableList()
                contextList?.add(1, episode.toString())
                contextList?.add(3, media.title.userPreferred)
                contextList?.joinToString("") ?: ""
            }
        }
    }

    fun getNotificationImage(): String {
        return user?.avatar?.large ?: media.coverImage.large
    }
}

