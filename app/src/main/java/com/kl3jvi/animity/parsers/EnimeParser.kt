package com.kl3jvi.animity.parsers

import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import javax.inject.Inject

class EnimeParser @Inject constructor() : BaseParser() {
    override suspend fun loadEpisodes(
        response: String,
        extra: Map<String, String>?
    ): List<EpisodeModel> {
        TODO("Not yet implemented")
    }

    override suspend fun loadVideoServers(
        episodeLink: String,
        extra: Map<String, String>?
    ): List<String> {
        TODO("Not yet implemented")
    }
}
