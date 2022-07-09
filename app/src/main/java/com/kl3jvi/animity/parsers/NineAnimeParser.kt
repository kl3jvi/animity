package com.kl3jvi.animity.parsers

import com.kl3jvi.animity.data.model.ui_models.AnimeInfoModel
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import javax.inject.Inject

class NineAnimeParser @Inject constructor() : BaseParser() {
    override fun fetchEpisodeList(response: String): List<EpisodeModel> {
        TODO("Not yet implemented")

    }

    override fun getMediaUrls(response: String): List<String> {
        TODO("Not yet implemented")
    }

    override fun parseAnimeInfo(response: String): AnimeInfoModel {
        TODO("Not yet implemented")
    }

    override fun parseEncryptedUrls(response: String): List<String> {
        TODO("Not yet implemented")
    }

}