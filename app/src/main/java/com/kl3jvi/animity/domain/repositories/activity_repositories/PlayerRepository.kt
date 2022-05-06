package com.kl3jvi.animity.domain.repositories.activity_repositories

import com.kl3jvi.animity.data.model.ui_models.EpisodeInfo
import com.kl3jvi.animity.utils.parser.Parser

interface PlayerRepository {
    val parser: Parser
    suspend fun fetchEpisodeMediaUrl(header: Map<String, String>, url: String): EpisodeInfo
    suspend fun fetchM3u8Url(header: Map<String, String>, url: String): ArrayList<String>
    suspend fun fetchEncryptedAjaxUrl(header: Map<String, String>, url: String, id: String): String
}