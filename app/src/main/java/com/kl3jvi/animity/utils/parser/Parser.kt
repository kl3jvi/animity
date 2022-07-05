package com.kl3jvi.animity.utils.parser

import com.kl3jvi.animity.data.model.ui_models.*
import org.jsoup.select.Elements

interface Parser {
    fun parseAnimeInfo(response: String): AnimeInfoModel
    fun getGenreList(genreHtmlList: Elements): List<GenreModel>
    fun fetchEpisodeList(response: String): List<EpisodeModel>
    fun decryptAES(encrypted: String, key: String, iv: String): String
    fun encryptAes(text: String, key: String, iv: String): String
    fun parseEncryptAjax(response: String, id: String): String
    fun parseMediaUrl(response: String): EpisodeInfo
    fun parseEncryptedUrls(response: String): List<String>
}
