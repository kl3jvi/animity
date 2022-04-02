package com.kl3jvi.animity.domain.repositories.fragment_repositories

import com.kl3jvi.animity.data.model.ui_models.HomeData
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.utils.NetworkResource
import com.kl3jvi.animity.utils.parser.HtmlParser
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    val parser: HtmlParser
    suspend fun fetchRecentSubOrDub(
        header: Map<String, String>,
        page: Int,
        type: Int
    ): List<AnimeMetaModel>

    suspend fun fetchPopularFromAjax(
        header: Map<String, String>,
        page: Int
    ): List<AnimeMetaModel>

    suspend fun fetchNewSeason(
        header: Map<String, String>,
        page: Int
    ): List<AnimeMetaModel>

    suspend fun fetchMovies(
        header: Map<String, String>,
        page: Int
    ): List<AnimeMetaModel>

    fun getHomeData(): Flow<NetworkResource<HomeData>>
}
