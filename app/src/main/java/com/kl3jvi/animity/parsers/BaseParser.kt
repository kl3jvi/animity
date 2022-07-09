package com.kl3jvi.animity.parsers

import com.kl3jvi.animity.data.model.ui_models.AnimeInfoModel
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel

abstract class BaseParser {
    abstract fun fetchEpisodeList(response: String): List<EpisodeModel>
    abstract fun getMediaUrls(response: String): List<String>
    abstract fun parseAnimeInfo(response: String): AnimeInfoModel
    abstract fun parseEncryptedUrls(response: String): List<String>
}
