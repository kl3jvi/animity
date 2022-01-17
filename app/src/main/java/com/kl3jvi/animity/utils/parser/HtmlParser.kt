package com.kl3jvi.animity.utils.parser

import android.os.Build
import android.util.Log
import com.kl3jvi.animity.data.model.*
import org.apache.commons.lang3.RandomStringUtils
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.net.URLDecoder
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.collections.ArrayList

/**
 *  This File gets response in String format and parses it
 */

object HtmlParser {
    fun parseRecentSubOrDub(response: String, typeValue: Int): ArrayList<AnimeMetaModel> {
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
                    id = title.hashCode(),
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

    fun parsePopular(response: String, typeValue: Int): ArrayList<AnimeMetaModel> {
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

    fun parseMovie(response: String, typeValue: Int): ArrayList<AnimeMetaModel> {
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

    fun parseAnimeInfo(response: String): AnimeInfoModel {
        val document = Jsoup.parse(response)
        val animeInfo = document.getElementsByClass("anime_info_body_bg")
        val animeUrl = animeInfo.select("img").first().absUrl("src")
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

    private fun getGenreList(genreHtmlList: Elements): ArrayList<GenreModel> {
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

    fun fetchEpisodeList(response: String): ArrayList<EpisodeModel> {
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
                    episodeUrl = episodeUrl
                )
            )
        }
        return episodeList
    }

    fun fetchEpisodeReleaseTime(response: String): EpisodeReleaseModel {
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

    private fun decryptAES(encrypted: String, key: String, iv: String): String {
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
                        android.util.Base64.DEFAULT
                    )
                )
            )
        }
    }

    private fun encryptAes(text: String, key: String, iv: String): String {
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


    fun parseEncryptAjax(response: String): String {
        val document = Jsoup.parse(response)
        val value6 = document.getElementsByAttributeValue("data-name", "ts").attr("data-value")
        val value5 = document.getElementsByAttributeValue("name", "crypto").attr("content")
        val value1 = decryptAES(
            document.getElementsByAttributeValue("data-name", "crypto").attr("data-value"),
            URLDecoder.decode(value6 + value6, Charsets.UTF_8.name()),
            URLDecoder.decode(value6, Charsets.UTF_8.name())
        )
        val value4 = decryptAES(
            value5,
            URLDecoder.decode(value1, Charsets.UTF_8.name()),
            URLDecoder.decode(value6, Charsets.UTF_8.name())
        )
        val value2 = RandomStringUtils.randomAlphanumeric(16)
        val value3 = URLDecoder.decode(value4, Charsets.UTF_8.name()).toString()
        val encrypted = encryptAes(
            value4.removeRange(value4.indexOf("&"), value4.length),
            URLDecoder.decode(value1, Charsets.UTF_8.name()),
            URLDecoder.decode(value2, Charsets.UTF_8.name())
        )
        return "id=" + encrypted + "&time=" + "00" + value2 + "00" + value3.substring(
            value3.indexOf(
                "&"
            )
        )
    }

    fun parseMediaUrl(response: String): EpisodeInfo {
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

    fun parseEncryptedUrls(response: String): ArrayList<String> {
        val urls: ArrayList<String> = ArrayList()
        var i = 0
        val res = JSONObject(response).getJSONArray("source")
        Log.e("resu,", res.toString())
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

private fun getCategoryUrl(url: String): String {
    return try {
        var categoryUrl = url.substring(url.lastIndexOf('/') + 1, url.lastIndexOf('.'))
        categoryUrl = "/category/$categoryUrl"
        categoryUrl
    } catch (exception: StringIndexOutOfBoundsException) {
        exception.message
    }.toString()
}

private fun filterGenreName(genreName: String): String {
    return if (genreName.contains(',')) {
        genreName.substring(genreName.indexOf(',') + 1)
    } else {
        genreName
    }
}

private fun formatInfoValues(infoValue: String): String {
    return infoValue.substring(infoValue.indexOf(':') + 1, infoValue.length)
}

