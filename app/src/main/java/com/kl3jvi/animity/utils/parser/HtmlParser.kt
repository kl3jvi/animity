package com.kl3jvi.animity.utils.parser

import com.kl3jvi.animity.model.entities.AnimeMetaModel
import com.kl3jvi.animity.model.entities.GenreModel
import org.jsoup.Jsoup
import org.jsoup.select.Elements


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
                    ID = title.hashCode(),
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
                    ID = "$animeTitle$typeValue".hashCode(),
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
                    ID = "$movieName$typeValue".hashCode().hashCode(),
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
}
