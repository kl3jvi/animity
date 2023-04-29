package com.kl3jvi.animity.domain.repositories

import com.kl3jvi.animity.data.model.ui_models.ChangedMediaResponse
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import com.kl3jvi.animity.parsers.GoGoParser
import com.kl3jvi.animity.type.MediaListStatus
import com.kl3jvi.animity.utils.Constants
import kotlinx.coroutines.flow.Flow

interface DetailsRepository {
    val parser: GoGoParser

    fun fetchEpisodeList(
        header: Map<String, String> = Constants.getNetworkHeader(),
        extra: List<Any?> = emptyList(),
        malId: Int,
        episodeUrl: String
    ): Flow<List<EpisodeModel>>

    fun changeAnimeStatus(
        mediaId: Int, status: MediaListStatus
    ): Flow<ChangedMediaResponse>
}
