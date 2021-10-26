package com.kl3jvi.animity.domain

import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Resource
import com.kl3jvi.animity.utils.parser.HtmlParser
import com.kl3jvi.animity.view.activities.player.PlayerRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetEpisodeInfoUseCase @Inject constructor(private val playerRepository: PlayerRepository) {

    fun fetchEpisodeMediaUrl(url: String) = flow {
        emit(Resource.Loading())
        try {
            val response = HtmlParser.parseMediaUrl(
                playerRepository.fetchEpisodeMediaUrl(
                    Constants.getHeader(),
                    url
                ).string()
            )
            emit(Resource.Success(data = response))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage))
        }
    }

    fun fetchM3U8(url: String) = flow {
        emit(Resource.Loading())
        try {
            val response = HtmlParser.parseM3U8Url(
                playerRepository.fetchEpisodeMediaUrl(
                    Constants.getHeader(),
                    url
                ).string()
            )
            emit(Resource.Success(data = response))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage))
        }
    }


}