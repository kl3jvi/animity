package com.kl3jvi.animity.view.fragments.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.kl3jvi.animity.model.entities.AnimeInfoModel
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Resource
import com.kl3jvi.animity.utils.parser.HtmlParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val detailsRepository: DetailsRepository
) : ViewModel() {

    val data: MutableLiveData<AnimeInfoModel> = MutableLiveData()


    fun fetchAnimeInfo(url: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = HtmlParser.parseAnimeInfo(
                        detailsRepository.fetchAnimeInfo(
                            Constants.getHeader(),
                            url
                        ).string()
                    )
                )
            )
            data.postValue(
                HtmlParser.parseAnimeInfo(
                    detailsRepository.fetchAnimeInfo(
                        Constants.getHeader(),
                        url
                    ).string()
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "error Occurred!"))
        }
    }


    fun fetchEpisodeList(
        id: String,
        endEpisode: String,
        alias: String
    ) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = HtmlParser.fetchEpisodeList(
                        detailsRepository.fetchEpisodeList(
                            header = Constants.getHeader(),
                            id = id,
                            endEpisode = endEpisode,
                            alias = alias
                        ).string()
                    )
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "error Occurred!"))
        }
    }


}

