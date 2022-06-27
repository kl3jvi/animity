package com.kl3jvi.animity.parsers

import com.kl3jvi.animity.data.model.ui_models.EpisodeModel

abstract class BaseParser {
    open val name: String = ""
    open val url: String = ""

    abstract suspend fun fetchEpisodeList(response: String): List<EpisodeModel>
    abstract suspend fun getMediaUrls(response: String): List<String>
}
