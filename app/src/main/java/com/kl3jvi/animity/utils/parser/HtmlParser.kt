package com.kl3jvi.animity.utils.parser

import android.os.Build
import com.kl3jvi.animity.data.model.ui_models.*
import com.kl3jvi.animity.domain.repositories.persistence_repositories.LocalStorage
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject


interface Parser {
    fun parseRecentSubOrDub(response: String, typeValue: Int): ArrayList<AnimeMetaModel>
    fun parsePopular(response: String, typeValue: Int): ArrayList<AnimeMetaModel>
    fun parseMovie(response: String, typeValue: Int): ArrayList<AnimeMetaModel>
    fun parseAnimeInfo(response: String): AnimeInfoModel
    fun getGenreList(genreHtmlList: Elements): ArrayList<GenreModel>
    fun fetchEpisodeList(response: String): ArrayList<EpisodeModel>
    fun fetchEpisodeReleaseTime(response: String): EpisodeReleaseModel
    fun decryptAES(encrypted: String, key: String, iv: String): String
    fun encryptAes(text: String, key: String, iv: String): String
    fun parseEncryptAjax(response: String, id: String): String
    fun parseMediaUrl(response: String): EpisodeInfo
    fun parseEncryptedUrls(response: String): ArrayList<String>
}

class HtmlParser @Inject constructor(
    private val preferences: LocalStorage
) : Parser {


    /**
     * It takes a response from a network request, parses it, and returns a list of AnimeMetaModel
     * objects
     *
     * @param response The response from the server.
     * @param typeValue This is the type of anime, whether it's subbed or dubbed.
     * @return An ArrayList of AnimeMetaModel objects.
     */
    override fun parseRecentSubOrDub(response: String, typeValue: Int): ArrayList<AnimeMetaModel> {
        val animeMetaModelList: ArrayList<AnimeMetaModel> = ArrayList()
        val document = Jsoup.parse(response)
        val lists = document?.getElementsByClass("items")?.first()?.select("li")
        var i = 0
        lists?.forEach { anime ->
            val animeInfo = anime.getElementsByClass("name").first().select("a")
            val title = animeInfo.attr("title")
            val episodeUrl = animeInfo.attr("href")
            val episodeNumber = anime.getElementsByClass("episode").first().text()
            val animeImageInfo = anime.selectFirst("a")
            val imageUrl = animeImageInfo.select("img").first().absUrl("src")
            animeMetaModelList.add(
                AnimeMetaModel(
                    id = title.lowercase(Locale.getDefault()).hashCode(),
                    title = title,
                    episodeNumber = episodeNumber,
                    episodeUrl = episodeUrl,
                    categoryUrl = getCategoryUrl(imageUrl),
                    imageUrl = imageUrl,
                    typeValue = typeValue,
                    insertionOrder = i
                )
            )
            i++
        }
        return animeMetaModelList
    }

    /**
     * It takes a response from the server, parses it, and returns a list of AnimeMetaModel objects
     *
     * @param response The response from the server.
     * @param typeValue This is the type of anime. It can be either 1 for ongoing or 2 for completed.
     * @return An ArrayList of AnimeMetaModel
     */
    override fun parsePopular(response: String, typeValue: Int): ArrayList<AnimeMetaModel> {
        val animeMetaModelList: ArrayList<AnimeMetaModel> = ArrayList()
        val document = Jsoup.parse(response)
        val lists = document?.getElementsByClass("added_series_body popular")?.first()?.select("ul")
            ?.first()?.select("li")
        var i = 0

        lists?.forEach { anime ->
            val animeInfoFirst = anime.select("a").first()
            val imageDiv =
                animeInfoFirst.getElementsByClass("thumbnail-popular").first().attr("style")
                    .toString()
            val imageUrl =
                imageDiv.substring(imageDiv.indexOf('\'') + 1, imageDiv.lastIndexOf('\''))
            val categoryUrl = animeInfoFirst.attr("href")
            val animeTitle = animeInfoFirst.attr("title")
            val animeInfoSecond = anime.select("p").last().select("a")
            val episodeUrl = animeInfoSecond.attr("href")
            val episodeNumber = animeInfoSecond.text()
            val genreHtmlList = anime.getElementsByClass("genres").first().select("a")
            val genreList = ArrayList<GenreModel>()
            genreList.addAll(getGenreList(genreHtmlList))
            animeMetaModelList.add(
                AnimeMetaModel(
                    id = "$animeTitle$typeValue".hashCode(),
                    title = animeTitle,
                    episodeNumber = episodeNumber,
                    episodeUrl = episodeUrl,
                    categoryUrl = categoryUrl,
                    imageUrl = imageUrl,
                    typeValue = typeValue,
                    genreList = genreList,
                    insertionOrder = i
                )
            )
            i++
        }
        return animeMetaModelList
    }

    /**
     * It takes a response from the server, parses it, and returns a list of AnimeMetaModel objects
     *
     * @param response The response from the server.
     * @param typeValue This is the type of the anime. It can be either 1, 2, 3, 4, 5, 6
     * @return A list of AnimeMetaModel
     */
    override fun parseMovie(response: String, typeValue: Int): ArrayList<AnimeMetaModel> {
        val animeMetaModelList: ArrayList<AnimeMetaModel> = ArrayList()
        val document = Jsoup.parse(response)
        val lists = document?.getElementsByClass("items")?.first()?.select("li")
        var i = 0
        lists?.forEach {
            val movieInfo = it.select("a").first()
            val movieUrl = movieInfo.attr("href")
            val movieName = movieInfo.attr("title")
            val imageUrl = movieInfo.select("img").first().absUrl("src")
            val releasedDate = it.getElementsByClass("released")?.first()?.text()
            animeMetaModelList.add(
                AnimeMetaModel(
                    id = "$movieName$typeValue".hashCode().hashCode(),
                    title = movieName,
                    imageUrl = imageUrl,
                    categoryUrl = movieUrl,
                    episodeUrl = null,
                    episodeNumber = null,
                    typeValue = typeValue,
                    insertionOrder = i,
                    releasedDate = releasedDate
                )
            )
            i++
        }
        return animeMetaModelList
    }

    /**
     * It parses the HTML response and returns an AnimeInfoModel object
     *
     * @param response The response from the server
     * @return An AnimeInfoModel object
     */
    override fun parseAnimeInfo(response: String): AnimeInfoModel {
        val document = Jsoup.parse(response)
        val animeInfo = document.getElementsByClass("anime_info_body_bg")
        val animeUrl = animeInfo.select("img").first().absUrl("src").orEmpty()
        val animeTitle = animeInfo.select("h1").first().text()
        val lists = document?.getElementsByClass("type")
        lateinit var type: String
        lateinit var releaseTime: String
        lateinit var status: String
        lateinit var plotSummary: String
        val genre: ArrayList<GenreModel> = ArrayList()
        lists?.forEachIndexed { index, element ->
            when (index) {
                0 -> type = element.text()
                1 -> plotSummary = element.text()
                2 -> genre.addAll(getGenreList(element.select("a")))
                3 -> releaseTime = element.text()
                4 -> status = element.text()
            }
        }
        val episodeInfo = document.getElementById("episode_page")
        val episodeList = episodeInfo.select("a").last()
        val endEpisode = episodeList.attr("ep_end")
        val alias = document.getElementById("alias_anime").attr("value")
        val id = document.getElementById("movie_id").attr("value")
        return AnimeInfoModel(
            id = id,
            animeTitle = animeTitle,
            imageUrl = animeUrl,
            type = formatInfoValues(type),
            releasedTime = formatInfoValues(releaseTime),
            status = formatInfoValues(status),
            genre = genre,
            plotSummary = formatInfoValues(plotSummary).trim(),
            alias = alias,
            endEpisode = endEpisode
        )
    }

    /**
     * It takes a list of HTML elements and returns a list of GenreModel objects.
     *
     * @param genreHtmlList The list of genres in the HTML document.
     * @return An ArrayList of GenreModel objects.
     */
    override fun getGenreList(genreHtmlList: Elements): ArrayList<GenreModel> {
        val genreList = ArrayList<GenreModel>()
        genreHtmlList.forEach {
            val genreUrl = it.attr("href")
            val genreName = it.text()

            genreList.add(
                GenreModel(
                    genreUrl = genreUrl,
                    genreName = filterGenreName(genreName)
                )
            )
        }
        return genreList
    }

    /**
     * It takes a string as an input, parses it using Jsoup, and returns an arraylist of EpisodeModel
     * objects
     *
     * @param response The response from the server.
     * @return An ArrayList of EpisodeModel objects.
     */
    override fun fetchEpisodeList(response: String): ArrayList<EpisodeModel> {
        val episodeList = ArrayList<EpisodeModel>()
        val document = Jsoup.parse(response)
        val lists = document?.select("li")
        lists?.forEach {
            val episodeUrl = it.select("a").first().attr("href").trim()
            val episodeNumber = it.getElementsByClass("name").first().text()
            val episodeType = it.getElementsByClass("cate").first().text()
            episodeList.add(
                EpisodeModel(
                    episodeNumber = episodeNumber,
                    episodeType = episodeType,
                    episodeUrl = episodeUrl,
                )
            )
        }
        return episodeList
    }

    /**
     * It takes a string as an input, parses it using Jsoup, and returns an EpisodeReleaseModel object
     *
     * @param response The response from the server.
     * @return A list of EpisodeReleaseModel objects.
     */
    override fun fetchEpisodeReleaseTime(response: String): EpisodeReleaseModel {
        val document = Jsoup.parse(response)
        var episodeNumber = ""
        var time = ""
        try {
            episodeNumber =
                document.getElementsByClass("release-time-type-text release-time-type-raw").first()
                    .text()
            time = document.getElementsByClass("release-time").first().text()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return EpisodeReleaseModel(
            episodeTitle = episodeNumber,
            time = time
        )
    }

    /**
     * Decrypts a string using AES
     *
     * @param encrypted The encrypted string
     * @param key "1234567890123456"
     * @param iv `"0000000000000000"`
     * @return The decrypted string
     */
    override fun decryptAES(encrypted: String, key: String, iv: String): String {
        val ix = IvParameterSpec(iv.toByteArray())
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val secretKey = SecretKeySpec(key.toByteArray(Charsets.UTF_8), "AES")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ix)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String(cipher.doFinal(Base64.getDecoder().decode(encrypted)))
        } else {
            String(
                cipher.doFinal(
                    android.util.Base64.decode(
                        encrypted,
                        android.util.Base64.URL_SAFE
                    )
                )
            )
        }
    }


    /**
     * Encrypts a string using AES encryption.
     *
     * @param text The text to be encrypted
     * @param key The key used to encrypt the data.
     * @param iv Initialization vector. This is a random string that is used to encrypt the data.
     * @return The encrypted text
     */
    override fun encryptAes(text: String, key: String, iv: String): String {
        val ix = IvParameterSpec(iv.toByteArray())
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val secretKey = SecretKeySpec(key.toByteArray(), "AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ix)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getEncoder().encodeToString(cipher.doFinal(text.toByteArray()))
        } else {
            android.util.Base64.encodeToString(
                cipher.doFinal(text.toByteArray()),
                android.util.Base64.DEFAULT
            )
        }
    }


    /**
     * It takes in a response and an id, and returns a string.
     *
     * @param response The response from the server.
     * @param id The id of the episode you want to watch.
     * @return The encrypted string
     */
    override fun parseEncryptAjax(response: String, id: String): String {
        return try {
            val document = Jsoup.parse(response)
            val value2 = document.select("script[data-name=\"episode\"]").attr("data-value")
            val decrypt =
                decryptAES(
                    value2,
                    preferences.key.toString(),
                    preferences.iv.toString()
                ).replace("\t", "").substringAfter(id)
            val encrypted = encryptAes(
                id, preferences.key.toString(),
                preferences.iv.toString()
            )
            "id=$encrypted$decrypt&alias=$id"
        } catch (e: java.lang.Exception) {
            e.toString()
        }
    }

    /**
     * It takes a string as input, parses it using Jsoup, and returns an object of type EpisodeInfo
     *
     * @param response The response from the server.
     * @return EpisodeInfo
     */
    override fun parseMediaUrl(response: String): EpisodeInfo {
        val mediaUrl: String?
        val document = Jsoup.parse(response)
        val info = document?.getElementsByClass("vidcdn")?.first()?.select("a")
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
     * It takes a response from the server, decrypts it, parses it, and returns an array of URLs
     *
     * @param response The response from the server
     * @return an arraylist of urls.
     */
    override fun parseEncryptedUrls(response: String): ArrayList<String> {
        val urls: ArrayList<String> = ArrayList()
        var i = 0
        val data = JSONObject(response).getString("data")
        val decryptedData = decryptAES(
            data,
            preferences.secondKey.toString(),
            preferences.iv.toString()
        ).replace(
            """o"<P{#meme":""",
            """e":[{"file":"""
        )
        val res = JSONObject(decryptedData).getJSONArray("source")
        return try {
            while (i != res.length() && res.getJSONObject(i).getString("label") != "Auto") {
                urls.add(res.getJSONObject(i).getString("file"))
                i++
            }
            urls
        } catch (exp: java.lang.NullPointerException) {
            urls
        }


    }
}

/**
 * It takes a url as a parameter and returns the category url
 *
 * @param url The URL of the page to be scraped.
 * @return The categoryUrl is being returned.
 */
private fun getCategoryUrl(url: String): String {
    return try {
        var categoryUrl = url.substring(url.lastIndexOf('/') + 1, url.lastIndexOf('.'))
        categoryUrl = "/category/$categoryUrl"
        categoryUrl
    } catch (exception: StringIndexOutOfBoundsException) {
        exception.message
    }.toString()
}

/**
 * If the genre name contains a comma, return the substring after the comma, otherwise return the genre
 * name
 *
 * @param genreName The name of the genre.
 * @return The genre name is being returned.
 */
private fun filterGenreName(genreName: String): String {
    return if (genreName.contains(',')) {
        genreName.substring(genreName.indexOf(',') + 1)
    } else {
        genreName
    }
}

/**
 * It takes a string, finds the first colon, and returns the substring after the colon
 *
 * @param infoValue The value of the info field.
 * @return the substring of the infoValue parameter.
 */
private fun formatInfoValues(infoValue: String): String {
    return infoValue.substring(infoValue.indexOf(':') + 1, infoValue.length)
}



