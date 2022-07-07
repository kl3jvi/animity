package com.kl3jvi.animity.parsers

import com.kl3jvi.animity.data.model.ui_models.AnimeInfoModel
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import org.jsoup.Jsoup
import javax.inject.Inject

class GoGoParser @Inject constructor() : BaseParser() {


    override suspend fun fetchEpisodeList(response: String): List<EpisodeModel> {
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

    override suspend fun getMediaUrls(response: String): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun parseAnimeInfo(response: String): AnimeInfoModel {
        TODO("Not yet implemented")
    }
}