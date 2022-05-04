package com.kl3jvi.animity.utils.parser

import com.kl3jvi.animity.data.model.ui_models.EpisodeInfo
import org.json.JSONObject
import org.jsoup.Jsoup

class StreamSB {

    companion object {

        /**
         * It converts a byte array to a hex string.
         *
         * @param bytes The byte array to convert to hex
         * @return A string of hexadecimal characters.
         */
        private fun bytesToHex(bytes: ByteArray): String {
            val hexArray = "0123456789ABCDEF".toCharArray()
            val hexChars = CharArray(bytes.size * 2)
            for (j in bytes.indices) {
                val v = bytes[j].toInt() and 0xFF

                hexChars[j * 2] = hexArray[v ushr 4]
                hexChars[j * 2 + 1] = hexArray[v and 0x0F]
            }
            return String(hexChars)
        }

        /**
         * It takes a string as input, parses it using Jsoup, and returns an object of type EpisodeInfo
         *
         * @param response The response from the server.
         * @return EpisodeInfo
         */
        fun parseMediaUrl(response: String): EpisodeInfo {
            val mediaUrl: String?
            val document = Jsoup.parse(response)
            val info = document?.getElementsByClass("streamsb")?.first()?.select("a")
            mediaUrl = info?.attr("data-video").toString()
            val nextEpisodeUrl =
                document.getElementsByClass("anime_video_body_episodes_r")?.select("a")?.first()
                    ?.attr("href")
            val previousEpisodeUrl =
                document.getElementsByClass("anime_video_body_episodes_l")?.select("a")?.first()
                    ?.attr("href")

            return EpisodeInfo(
                nextEpisodeUrl = nextEpisodeUrl,
                previousEpisodeUrl = previousEpisodeUrl,
                vidCdnUrl = mediaUrl
            )
        }

        /**
         * It converts the url to a hex string and then adds it to the end of a string.
         *
         * @param url The url of the video you want to play
         * @return A URL
         */
        fun parseUrl(url: String): String {
            return "https://sbplay2.xyz/sources43/7361696b6f757c7c${
                bytesToHex(url.substringAfter("/e/").encodeToByteArray())
            }7c7c616e696d646c616e696d646c7c7c73747265616d7362/616e696d646c616e696d646c7c7c363136653639366436343663363136653639366436343663376337633631366536393664363436633631366536393664363436633763376336313665363936643634366336313665363936643634366337633763373337343732363536313664373336327c7c616e696d646c616e696d646c7c7c73747265616d7362"
        }

        /**
         * It parses the encrypted url from the response.
         *
         * @param response The response from the server.
         * @return The URL of the video
         */
        fun parseEncryptedUrls(response: String): ArrayList<String> {
            val urls: ArrayList<String> = ArrayList()
            var i = 0
            val res = JSONObject(response).getJSONArray("source")
            return try {
                while (i != res.length() && res.getJSONObject(i).getString("label") != "Auto") {
                    urls.add(res.getJSONObject(i).getString("file"))
                    i++
                }
                urls
            } catch (exp: NullPointerException) {
                urls
            }
        }
    }
}