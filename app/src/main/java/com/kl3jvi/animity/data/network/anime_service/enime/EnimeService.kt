package com.kl3jvi.animity.data.network.anime_service.enime

import com.kl3jvi.animity.data.model.ui_models.EnimeResponse
import com.kl3jvi.animity.data.model.ui_models.ZoroVideoSource
import retrofit2.http.GET
import retrofit2.http.Path

interface EnimeService {
    @GET("https://api.enime.moe/mapping/mal/{id}")
    suspend fun getEnimeEpisodesIds(@Path("id") malId: Int): EnimeResponse

    @GET("https://api.enime.moe/source/{source}")
    suspend fun getEnimeSource(@Path("source") source: String): ZoroVideoSource
}
