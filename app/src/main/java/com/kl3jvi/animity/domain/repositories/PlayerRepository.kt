package com.kl3jvi.animity.domain.repositories

import com.kl3jvi.animity.data.model.EpisodeInfo
import com.kl3jvi.animity.utils.parser.HtmlParser

interface PlayerRepository {
    val parser: HtmlParser
    suspend fun fetchEpisodeMediaUrl(header: Map<String, String>, url: String): EpisodeInfo
    suspend fun fetchM3u8Url(header: Map<String, String>, url: String):ArrayList<String>
    suspend fun fetchEncryptedAjaxUrl(header: Map<String, String>, url: String):String
}