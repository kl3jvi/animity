package com.kl3jvi.animity.parsers

import android.os.Build
import android.util.Log
import com.kl3jvi.animity.data.model.ui_models.AnimeInfoModel
import com.kl3jvi.animity.data.model.ui_models.EpisodeInfo
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import com.kl3jvi.animity.domain.repositories.PersistenceRepository
import org.json.JSONObject
import org.jsoup.Jsoup
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class GoGoParser @Inject constructor(
    private val preferences: PersistenceRepository
) : BaseParser() {

    override val name: String
        get() = "GOGO_ANIME"

    override fun fetchEpisodeList(response: String): List<EpisodeModel> {
        val document = Jsoup.parse(response)
        val lists = document.select("li")
        return lists.map {
            val episodeUrl = it.select("a").first()?.attr("href")?.trim()
            val episodeNumber = it.getElementsByClass("name").first()?.text()
            val episodeType = it.getElementsByClass("cate").first()?.text()
            EpisodeModel(
                episodeNumber = episodeNumber ?: "",
                episodeType = episodeType ?: "",
                episodeUrl = episodeUrl ?: ""
            )
        }
    }

    fun parseAnimeInfo(response: String): AnimeInfoModel {
        val document = Jsoup.parse(response)
        val episodeInfo = document.getElementById("episode_page")
        val episodeList = episodeInfo?.select("a")?.last()
        val endEpisode = episodeList?.attr("ep_end").orEmpty()
        val alias = document.getElementById("alias_anime")?.attr("value").orEmpty()
        val id = document.getElementById("movie_id")?.attr("value").orEmpty()
        return AnimeInfoModel(
            id = id,
            alias = alias,
            endEpisode = endEpisode
        )
    }

    fun parseMediaUrl(response: String): EpisodeInfo {
        val mediaUrl: String?
        val document = Jsoup.parse(response)
        Log.e("Parsed doc", document.toString())
        val info = document.getElementsByClass("vidcdn").first()?.select("a")
        mediaUrl = info?.attr("data-video").toString()
        val nextEpisodeUrl =
            document.getElementsByClass("anime_video_body_episodes_r").select("a").first()
                ?.attr("href")
        val previousEpisodeUrl =
            document.getElementsByClass("anime_video_body_episodes_l").select("a").first()
                ?.attr("href")

        return EpisodeInfo(
            nextEpisodeUrl = nextEpisodeUrl,
            previousEpisodeUrl = previousEpisodeUrl,
            vidCdnUrl = mediaUrl
        )
    }

    fun parseEncryptAjax(response: String, id: String): String {
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
                id,
                preferences.key.toString(),
                preferences.iv.toString()
            )
            "id=$encrypted$decrypt&alias=$id"
        } catch (e: java.lang.Exception) {
            e.toString()
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
                        android.util.Base64.URL_SAFE
                    )
                )
            )
        }
    }

    fun parseEncryptedUrls(response: String): List<String> {
        val urls = mutableListOf<String>()
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
        for (i in 0 until res.length()) {
            val label = res.getJSONObject(i).getString("label")
            if (label == "Auto") break
            urls.add(res.getJSONObject(i).getString("file"))
        }
        return urls
    }
}
