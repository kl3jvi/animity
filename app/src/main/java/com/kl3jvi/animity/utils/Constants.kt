package com.kl3jvi.animity.utils

import android.content.res.ColorStateList
import android.graphics.Color
import com.kl3jvi.animity.model.entities.AnimeMetaModel
import com.kl3jvi.animity.utils.parser.HtmlParser

class Constants {
    companion object {

        const val DATABASE_NAME = "anime_databbase"

        //Base URLS
        var BASE_URL = "https://gogoanime.pe"
        const val EPISODE_LOAD_URL = "https://ajax.gogocdn.net/ajax/load-list-episode"
        const val SEARCH_URL = "/search.html"

        //Model Type
        const val TYPE_RECENT_SUB = 1
        const val TYPE_POPULAR_ANIME = 2
        const val TYPE_RECENT_DUB = 3
        const val TYPE_GENRE = 4
        const val TYPE_MOVIE = 5
        const val TYPE_NEW_SEASON = 6
        const val TYPE_DEFAULT = -1


        val DETAILS_BACKGROUND = arrayOf(
            "https://wallpaperaccess.com/full/1961156.jpg",
            "https://wallpaperaccess.com/full/4835514.jpg",
            "https://wallpaperaccess.com/full/9928.png",
            "https://wallpaperaccess.com/full/9929.jpg",
            "https://wallpaperaccess.com/full/9939.jpg"
        )

        // Retrofit Request TYPE

        const val RECENT_SUB = 1
        const val RECENT_DUB = 2

        const val MAX_LIMIT_FOR_SUB_DUB = 10
        const val NEWEST_SEASON_POSITION = 2
        const val RECENT_SUB_POSITION = 0
        const val RECENT_DUB_POSITION = 1
        const val POPULAR_POSITION = 4
        const val MOVIE_POSITION = 3

        //Episode URL Type
        const val TYPE_MEDIA_URL = 100
        const val TYPE_M3U8_URL = 101

        //Anime Info URL Type
        const val TYPE_ANIME_INFO = 1000
        const val TYPE_EPISODE_LIST = 1001
        const val M3U8_REGEX_PATTERN =
            "(http|https)://([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?"

        //Anime Search Types
        const val TYPE_SEARCH_NEW = 2000
        const val TYPE_SEARCH_UPDATE = 2001

        //Network Requests Header
        const val USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36"
        const val ORIGIN = "https://gogoanime.vc/"
        const val REFERER = "https://gogoanime.vc/"

        //Realm
        const val MAX_TIME_M3U8_URL = 2 * 60 * 60 * 1000
        const val MAX_TIME_FOR_ANIME = 2 * 24 * 60 * 60 * 1000

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