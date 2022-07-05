package com.kl3jvi.animity.domain.repositories.activity_repositories

import com.kl3jvi.animity.data.model.ui_models.EpisodeInfo
import com.kl3jvi.animity.utils.Constants.Companion.getNetworkHeader
import com.kl3jvi.animity.utils.parser.Parser
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    val parser: Parser
    fun fetchEpisodeMediaUrl(
        header: Map<String, String> = getNetworkHeader(),
        url: String
    ): Flow<EpisodeInfo>

    fun fetchM3u8Url(
        header: Map<String, String> = getNetworkHeader(),
        url: String
    ): Flow<List<String>>

    fun fetchEncryptedAjaxUrl(
        header: Map<String, String> = getNetworkHeader(),
        url: String, id: String
    ): Flow<String>
}