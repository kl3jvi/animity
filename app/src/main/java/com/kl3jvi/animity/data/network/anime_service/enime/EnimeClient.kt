package com.kl3jvi.animity.data.network.anime_service.enime

import javax.inject.Inject

class EnimeClient @Inject constructor(
    private val enimeService: EnimeService
) {
    suspend fun getEnimeEpisodesIds(malId: Int) = enimeService.getEnimeEpisodesIds(malId)
    suspend fun getEnimeSource(source: String) = enimeService.getEnimeSource(source)
}
