package com.kl3jvi.animity.parsers

import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import com.kl3jvi.animity.utils.RetrofitClient
import okhttp3.OkHttpClient
import org.jsoup.Jsoup
import retrofit2.Retrofit
import javax.inject.Inject

class GoGoParser @Inject constructor(
    private val retrofit: Retrofit,
    @RetrofitClient val okHttpClient: OkHttpClient
) : BaseParser() {

    fun retrofit(): Retrofit {
        return retrofit.newBuilder()
            .client(okHttpClient)
            .build()
    }

    override val name: String
        get() = "GoGoAnime"

    override val url: String
        get() = "https://gogoanime.gg"

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
}