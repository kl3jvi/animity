package com.kl3jvi.animity.domain

import com.kl3jvi.animity.model.entities.AnimeInfoModel
import com.kl3jvi.animity.model.entities.EpisodeModel
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Resource
import com.kl3jvi.animity.utils.parser.HtmlParser
import com.kl3jvi.animity.view.fragments.details.DetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetAnimeDetailsUseCase @Inject constructor(private val detailsRepository: DetailsRepository) {

    fun fetchAnimeInfo(url: String): Flow<Resource<AnimeInfoModel>> = flow {
        try {
            emit(Resource.Loading())
            val response =
                HtmlParser.parseAnimeInfo(
                    detailsRepository.fetchAnimeInfo(
                        Constants.getHeader(),
                        url,
                    ).string()
                )
            emit(
                Resource.Success(
                    data = response
                )
            )
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage ?: "An unexpected error occurred",
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    e.localizedMessage ?: "Couldn't reach server. Check your internet connection.",
                )
            )
        }
    }

    fun fetchEpisodeList(
        id: String,
        endEpisode: String,
        alias: String
    ): Flow<Resource<List<EpisodeModel>>> = flow {
        try {
            emit(Resource.Loading())
            val response =
                HtmlParser.fetchEpisodeList(
                    detailsRepository.fetchEpisodeList(
                        header = Constants.getHeader(),
                        id = id,
                        endEpisode = endEpisode,
                        alias = alias
                    ).string()
                ).toList()
            emit(
                Resource.Success(
                    data = response
                )
            )
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage ?: "An unexpected error occurred"
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    e.localizedMessage ?: "Couldn't reach server. Check your internet connection."
                )
            )
        }
    }
}