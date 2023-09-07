package com.kl3jvi.animity.parsers

import android.util.Log
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import org.jsoup.Jsoup

class AniWaveParser : BaseParser() {

    override val name: String
        get() = "AniWave"

    override suspend fun fetchEpisodeList(response: String): List<EpisodeModel> {
        val document = Jsoup.parse(response)
        val animeId = document.select("#watch-main").attr("data-id")
        val body =
            Jsoup.connect("https://aniwave.to/ajax/episode/list/$animeId?vrf=${encodeVrf(animeId)}")
                .get()
        Log.e("Body", body.toString())

        val lists = document.select("YOUR_CSS_SELECTOR_FOR_EPISODE_LIST")
        return lists.map {
            val episodeUrl =
                it.select("YOUR_CSS_SELECTOR_FOR_EPISODE_URL").first()?.attr("href")?.trim()
            val episodeNumber = it.select("YOUR_CSS_SELECTOR_FOR_EPISODE_NUMBER").first()?.text()
            EpisodeModel(
                episodeNumber = episodeNumber ?: "",
                episodeUrl = episodeUrl ?: "",
            )
        }
    }

    private suspend fun encodeVrf(text: String): String {
        return Jsoup.connect("https://9anime.eltik.net/vrf?query=$text&apikey=saikou").get().text()
    }

    private suspend fun decodeVrf(text: String): String {
        return Jsoup.connect("https://9anime.eltik.net/decrypt?query=$text&apikey=saikou").get()
            .text()
    }
}
