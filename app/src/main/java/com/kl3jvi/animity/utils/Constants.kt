package com.kl3jvi.animity.utils

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.material.snackbar.Snackbar

class Constants {
    companion object {

        const val ANIME_TITLE: String = "ANIME_TITLE"
        const val EPISODE_DETAILS: String = "episodeInfo"
        const val MAL_ID = "anilist_id"

        // Base URLS
        const val GOGO_BASE_URL = "https://gogoanime.gr"
        const val ENIME_BASE_URL = "https://api.enime.moe"
        const val ANILIST_API_URL = "https://graphql.anilist.co"
        const val BILI_URL = "https://api-vn.kaguya.app/server"

        // Network Requests Header
        const val USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36"

        const val ORIGIN = "https://gogoanime.gg/"
        const val REFERER = "https://goload.pro/"

//        fun getHeader(): Map<String, String> {
//            return mapOf(
//                "referer" to REFERER,
//                "origin" to ORIGIN,
//                "user-agent" to USER_AGENT
//            )
//        }

        // Gogoanime Secrets
//        {"key": "37911490979715163134003223491201", "second_key": "54674138327930866480207815084989", "iv": "3134003223491201"}
        var GogoSecretkey = "37911490979715163134003223491201"
        var GogoSecretIV = "3134003223491201"
        var GogoSecretSecondKey = "54674138327930866480207815084989"
        val GogoPadding = byteArrayOf(0x8, 0xe, 0x3, 0x8, 0x9, 0x3, 0x4, 0x9)

        fun getSafeString(string: String?) = string.orEmpty()

        val String.Companion.Empty get() = ""

        /**
         * It returns a ColorStateList object.
         *
         * @return A ColorStateList object.
         */
        fun getColor(): ColorStateList {
            val color: Int = Color.argb(255, 4, 138, 129)
            return ColorStateList.valueOf(color)
        }

        /**
         * It creates a factory for the data source.
         *
         * @return A DefaultHttpDataSource.Factory object
         */
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
                    "sec-fetch-dest" to "video",
                ) + getNetworkHeader() // Adds the headers from the provider, e.g Authorization
                setDefaultRequestProperties(headers)
            }
        }

        /**
         * It shows a snackbar with the message passed to it.
         *
         * @param view View - The view to show the snackbar on
         * @param message The message to be displayed in the snackbar.
         */
        fun showSnack(view: View?, message: String?) {
            view?.let {
                val snack =
                    Snackbar.make(view, message ?: "Error Occurred", Snackbar.LENGTH_LONG)
                if (!snack.isShown) {
                    snack.show()
                }
            }
        }

        /**
         * It returns a map of the network headers.
         *
         * @return A map of the headers
         */
        fun getNetworkHeader(): Map<String, String> {
            return mapOf("referer" to REFERER, "origin" to ORIGIN, "user-agent" to USER_AGENT)
        }
    }
}
