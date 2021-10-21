package com.kl3jvi.animity.view.activities.player

import androidx.lifecycle.*
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Resource
import com.kl3jvi.animity.utils.parser.HtmlParser
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class PlayerViewModel @Inject constructor(private val playerRepository: PlayerRepository) : ViewModel() {

    private var _vidUrl = MutableLiveData<String>()
    var videoUrlLiveData: LiveData<String> = _vidUrl

    fun fetchEpisodeMediaUrl(url:String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = HtmlParser.parseMediaUrl(
                    playerRepository.fetchEpisodeMediaUrl(
                        Constants.getHeader(),
                        url
                    ).string())
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "error Occurred!"))
        }
    }


    fun fetchM3U8(url:String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = HtmlParser.parseM3U8Url(
                        playerRepository.fetchM3u8Url(
                            Constants.getHeader(),
                            url
                        ).string()
                    )
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "error Occurred!"))
        }
    }

    fun updateEpisodeUrl(vidUrl: String) {
        _vidUrl.value = vidUrl
    }

}
