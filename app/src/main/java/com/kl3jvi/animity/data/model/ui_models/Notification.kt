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

    val user: User = User(),
    /**
     * The associated media of the airing schedule
     */
    val media: AniListMedia = AniListMedia()
) {
    fun getFormattedNotification(): String {
        val contextList = contexts?.toMutableList()
        contextList?.add(1, episode.toString())
        contextList?.add(3, media.title.userPreferred)
        return contextList?.joinToString("") ?: ""
    }
}
