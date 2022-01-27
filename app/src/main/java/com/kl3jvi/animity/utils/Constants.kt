package com.kl3jvi.animity.utils

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.material.snackbar.Snackbar

class Constants {
    companion object {

        const val ONESIGNAL_APP_ID = "f8d936f4-2d9f-4c53-9f85-e2d3789d9174"
        const val INTRO_SKIP_TIME = 85000
        const val ANIME_TITLE: String = "ANIME_TITLE"
        const val EPISODE_DETAILS: String = "episodeInfo"
        const val DATABASE_NAME = "anime_database"
        const val TOKEN_PREFERENCES = "Animity_Token_Preferences"
        const val STARTING_PAGE_INDEX = 1
        const val AUTH_URL = "https://anilist.co/api/v2/oauth/token"

        // Base URLS
        const val BASE_URL = "https://gogoanime.film"
        const val ANILIST_API_URL = "https://graphql.anilist.co"

        const val EPISODE_LOAD_URL = "https://ajax.gogocdn.net/ajax/load-list-episode"
        const val SEARCH_URL = "/search.html"
        const val ANIME_SCHEDULE = "https://animeschedule.net/anime"
        const val AUTH_GRANT_TYPE = "authorization_code"
        const val REFRESH_GRANT_TYPE = "refresh_token"
        const val TERMS_AND_PRIVACY_LINK = "https://anilist.co/terms"
        const val SIGNUP_URL = "https://anilist.co/signup"


        // Model Type
        const val TYPE_RECENT_SUB = 1
        const val TYPE_POPULAR_ANIME = 2
        const val TYPE_RECENT_DUB = 3
        const val TYPE_MOVIE = 4
        const val TYPE_NEW_SEASON = 5
        const val TYPE_SEARCH = 6

        const val DOWNLOAD_CHANNEL_ID = "animity.channel.id"
        const val DOWNLOAD_CHANNEL_NAME = "Downloads"
        const val DOWNLOAD_CHANNEL_DESCRIPTION = "The download notification channel"

        const val GUEST_LOGIN_TYPE = "guest"
        const val AUTHENTICATED_LOGIN_TYPE = "authenticated"

        // Network Requests Header
        private const val USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36"

        private const val ORIGIN = "https://streamani.io"
        const val REFERER = "https://streamani.io/"

        fun getHeader(): Map<String, String> {
            return mapOf(
                "referer" to REFERER,
                "origin" to ORIGIN,
                "user-agent" to USER_AGENT
            )
        }

        fun getSafeString(string: String?) = string.toString()

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

        fun showSnack(view: View, message: String?) {
            val snack =
                Snackbar.make(view, message ?: "Error Occurred", Snackbar.LENGTH_LONG)
            if (!snack.isShown) {
                snack.show()
            }
        }


        fun getNetworkHeader(): Map<String, String> {
            return mapOf("referer" to REFERER, "origin" to ORIGIN, "user-agent" to USER_AGENT)
        }
    }
}

