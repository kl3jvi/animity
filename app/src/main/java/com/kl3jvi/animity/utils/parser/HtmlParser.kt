package com.kl3jvi.animity.utils.parser

import com.kl3jvi.animity.model.entities.AnimeMetaModel
import org.jsoup.Jsoup

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


    private fun getCategoryUrl(url: String): String {
        return try {
            var categoryUrl = url.substring(url.lastIndexOf('/') + 1, url.lastIndexOf('.'))
            categoryUrl = "/category/$categoryUrl"
            categoryUrl
        } catch (exception: StringIndexOutOfBoundsException) {
            exception.message
        }.toString()

    }
}
