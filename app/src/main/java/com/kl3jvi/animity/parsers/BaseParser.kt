package com.kl3jvi.animity.parsers

import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import java.net.URLDecoder
import java.net.URLEncoder

abstract class BaseParser {

    open val name: String = ""

    /**
     * Takes ShowResponse.link & ShowResponse.extra (if you added any) as arguments & gives a list of total episodes present on the site.
     * **/
    abstract suspend fun loadEpisodes(
        response: String,
        extra: Map<String, String>?
    ): List<EpisodeModel>

    /**
     * Takes Episode.link as a parameter
     *
     * This returns a Map of "Video Server's Name" & "Link/Data" of all the Video Servers present on the site, which can be further used by loadVideoServers() & loadSingleVideoServer()
     * **/
    abstract suspend fun loadVideoServers(
        episodeLink: String,
        extra: Map<String, String>?
    ): List<String>

    fun encode(input: String): String = URLEncoder.encode(input, "utf-8").replace("+", "%20")
    fun decode(input: String): String = URLDecoder.decode(input, "utf-8")
}
