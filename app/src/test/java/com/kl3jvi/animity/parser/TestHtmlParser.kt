package com.kl3jvi.animity.parser

import com.kl3jvi.animity.data.model.ui_models.AnimeInfoModel
import com.kl3jvi.animity.data.model.ui_models.EpisodeInfo
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import com.kl3jvi.animity.data.model.ui_models.GenreModel
import com.kl3jvi.animity.utils.parser.Parser
import org.jsoup.select.Elements

class TestHtmlParser : Parser {

    override fun parseAnimeInfo(response: String): AnimeInfoModel {
        return AnimeInfoModel(
            id = "1",
            animeTitle = "One Piece",
            imageUrl = "https://imageUrl.com",
            type = "Anime",
            releasedTime = "13:10:23",
            status = "Released",
            genre = arrayListOf(
                GenreModel(
                    genreUrl = "https://test.com",
                    genreName = "Fantasy"
                )
            ),
            plotSummary = "Lorem ipsum dolor sit amet",
            alias = "Test alias",
            endEpisode = "32"
        )
    }

    override fun getGenreList(genreHtmlList: Elements): ArrayList<GenreModel> {
        TODO("Not yet implemented")
    }

    override fun fetchEpisodeList(response: String): ArrayList<EpisodeModel> {
        TODO("Not yet implemented")
    }


    override fun decryptAES(encrypted: String, key: String, iv: String): String {
        TODO("Not yet implemented")
    }

    override fun encryptAes(text: String, key: String, iv: String): String {
        TODO("Not yet implemented")
    }

    override fun parseEncryptAjax(response: String, id: String): String {
        TODO("Not yet implemented")
    }

    override fun parseMediaUrl(response: String): EpisodeInfo {
        TODO("Not yet implemented")
    }

    override fun parseEncryptedUrls(response: String): ArrayList<String> {
        TODO("Not yet implemented")
    }
}