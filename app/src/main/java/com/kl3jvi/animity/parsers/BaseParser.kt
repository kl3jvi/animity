package com.kl3jvi.animity.parsers

import com.kl3jvi.animity.data.model.ui_models.AnimeInfoModel
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel

abstract class BaseParser {

    abstract suspend fun fetchEpisodeList(response: String): List<EpisodeModel>
    abstract suspend fun getMediaUrls(response: String): List<String>
    abstract suspend fun parseAnimeInfo(response: String): AnimeInfoModel
}
