package com.kl3jvi.animity.utils

import android.content.res.ColorStateList
import android.graphics.Color
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.kl3jvi.animity.model.AnimeMetaModel
import com.kl3jvi.animity.utils.parser.HtmlParser

class Constants {
    companion object {

        const val ONESIGNAL_APP_ID = "f8d936f4-2d9f-4c53-9f85-e2d3789d9174"
        const val INTRO_SKIP_TIME = 85000
        const val ANIME_TITLE: String = "ANIME_TITLE"
        const val EPISODE_DETAILS: String = "episodeInfo"
        const val DATABASE_NAME = "anime_database"
        const val SAVE_DIRECTORY = "Animity"

        // Base URLS
        var BASE_URL = "https://www1.gogoanime.cm/"
        const val EPISODE_LOAD_URL = "https://ajax.gogocdn.net/ajax/load-list-episode"
        const val SEARCH_URL = "/search.html"
        const val ANIME_SCHEDULE="https://animeschedule.net/anime"

        // Model Type
        private const val TYPE_RECENT_SUB = 1
        const val TYPE_POPULAR_ANIME = 2
        const val TYPE_RECENT_DUB = 3
        const val TYPE_MOVIE = 4
        const val TYPE_NEW_SEASON = 5

        const val DOWNLOAD_CHANNEL_ID = "animity.channel.id"
        const val DOWNLOAD_CHANNEL_NAME = "Downloads"
        const val DOWNLOAD_CHANNEL_DESCRIPTION = "The download notification channel"

        const val M3U8_REGEX_PATTERN =
            "(http|https)://([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?"

        // Network Requests Header
        private const val USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36"

        private const val ORIGIN = "https://goload.one"
        private const val  REFERER = "https://goload.one"

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

        fun getDataSourceFactory(): DefaultHttpDataSource.Factory {
            return DefaultHttpDataSource.Factory().apply {
                setUserAgent(USER_AGENT)
                val headers = mapOf(
                    "referer" to REFERER,
                    "accept" to "*/*",
                    "sec-ch-ua" to "\"Chromium\";v=\"91\", \" Not;A Brand\";v=\"99\"",
                    "sec-ch-ua-mobile" to "?0",
                    "sec-fetch-user" to "?1",
                    "sec-fetch-mode" to "navigate",
                    "sec-fetch-dest" to "video"
                ) + getHeader() // Adds the headers from the provider, e.g Authorization
                setDefaultRequestProperties(headers)
            }
        }

        fun getRandomId(): Int = System.currentTimeMillis().toInt()
    }
}
