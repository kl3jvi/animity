package com.kl3jvi.animity.model.entities

data class AnimeMetaModel(
    var typeValue: Int? = null,
    var imageUrl: String = "",
    var categoryUrl: String? = null,
    var episodeUrl: String? = null,
    var title: String = "",
    var episodeNumber: String? = null,
    var timestamp: Long = System.currentTimeMillis(),
    var insertionOrder: Int = -1,
    var releasedDate: String? = null

)