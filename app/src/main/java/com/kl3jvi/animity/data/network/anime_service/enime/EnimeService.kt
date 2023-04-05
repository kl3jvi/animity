package com.kl3jvi.animity.data.network.anime_service.enime

import com.kl3jvi.animity.data.model.ui_models.EnimeResponse
import com.kl3jvi.animity.data.model.ui_models.ZoroVideoSource
import com.kl3jvi.animity.data.network.anime_service.base.BaseService
import retrofit2.http.GET
import retrofit2.http.Path

interface EnimeService : BaseService {
    @GET("/mapping/mal/{id}")
    suspend fun getEnimeEpisodesIds(@Path("id") malId: Int): EnimeResponse

    @GET("/source/{source}")
    suspend fun getEnimeSource(@Path("source") source: String): ZoroVideoSource
}
