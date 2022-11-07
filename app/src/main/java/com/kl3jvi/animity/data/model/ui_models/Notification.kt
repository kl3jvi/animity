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
     * The associated media of the airing schedule
     */
    val media: NotificationMedia
)
