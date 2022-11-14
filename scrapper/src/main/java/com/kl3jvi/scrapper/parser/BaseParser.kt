package com.kl3jvi.scrapper.parser

abstract class BaseParser {
    abstract fun fetchEpisodeList(response: String): List<EpisodeModel>
    abstract fun getMediaUrls(response: String): List<String>
    abstract fun parseAnimeInfo(response: String): AnimeInfoModel
    abstract fun parseEncryptedUrls(response: String): List<String>
}
