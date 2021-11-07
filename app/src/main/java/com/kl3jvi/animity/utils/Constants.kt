package com.kl3jvi.animity.utils

import android.content.res.ColorStateList
import android.graphics.Color
import com.kl3jvi.animity.model.entities.AnimeMetaModel
import com.kl3jvi.animity.utils.parser.HtmlParser

class Constants {
    companion object {

        const val INTRO_SKIP_TIME = 85000
        const val ANIME_TITLE: String = "ANIME_TITLE"
        const val EPISODE_DETAILS: String = "episodeInfo"
        const val DATABASE_NAME = "anime_database"

        //Base URLS
        var BASE_URL = "https://gogoanime.cm"
        const val EPISODE_LOAD_URL = "https://ajax.gogocdn.net/ajax/load-list-episode"
        const val SEARCH_URL = "/search.html"

        //Model Type
        private const val TYPE_RECENT_SUB = 1
        const val TYPE_POPULAR_ANIME = 2
        const val TYPE_RECENT_DUB = 3
        const val TYPE_MOVIE = 4
        const val TYPE_NEW_SEASON = 5

        const val DOWNLOAD_CHANNEL_ID = "animity.channel.id"
        const val DOWNLOAD_CHANNEL_NAME = "Downloads"
        const val DOWNLOAD_CHANNEL_DESCRIPT = "The download notification channel"

        const val M3U8_REGEX_PATTERN =
            "(http|https)://([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?"

        //Network Requests Header
        const val USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36"

        const val ORIGIN = "https://goload.one"
        const val REFERER = "https://goload.one"

        fun getHeader(): Map<String, String> {
            return mapOf(
                "referer" to REFERER,
                "origin" to ORIGIN,
                "user-agent" to USER_AGENT
            )
        }

        fun getColor(): ColorStateList {
            val color: Int = Color.argb(255, 4, 138, 129)
            return ColorStateList.valueOf(color)
        }

        fun getBackgroundColor(): ColorStateList {
            val color: Int = Color.argb(255, 17, 17, 17)
            return ColorStateList.valueOf(color)
        }

        fun getVerticalAdapterBackgroundColor(): ColorStateList {
            val color: Int = Color.argb(255, 17, 17, 17)
            return ColorStateList.valueOf(color)
        }

        fun parseList(response: String, typeValue: Int): ArrayList<AnimeMetaModel> {
            return when (typeValue) {
                TYPE_RECENT_DUB -> HtmlParser.parseRecentSubOrDub(response, typeValue)
                TYPE_RECENT_SUB -> HtmlParser.parseRecentSubOrDub(response, typeValue)
                TYPE_POPULAR_ANIME -> HtmlParser.parsePopular(response, typeValue)
                TYPE_MOVIE -> HtmlParser.parseMovie(response, typeValue)
                TYPE_NEW_SEASON -> HtmlParser.parseMovie(response, typeValue)
                else -> ArrayList()
            }
        }
    }
}