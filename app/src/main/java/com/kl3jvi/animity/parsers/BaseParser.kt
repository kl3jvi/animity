package com.kl3jvi.animity.parsers

import com.kl3jvi.animity.data.model.ui_models.EpisodeModel

abstract class BaseParser {
    open val name: String = ""
    abstract fun fetchEpisodeList(response: String): List<EpisodeModel>
}
