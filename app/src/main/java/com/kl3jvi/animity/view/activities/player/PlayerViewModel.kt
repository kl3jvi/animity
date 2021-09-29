package com.kl3jvi.animity.view.activities.player

import androidx.lifecycle.*
import com.kl3jvi.animity.model.entities.Content
import com.kl3jvi.animity.model.network.ApiHelper
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Resource
import kotlinx.coroutines.Dispatchers

class PlayerViewModel(private val playerRepository: PlayerRepository) : ViewModel() {

    private var _content = MutableLiveData(Content())
    var liveContent: LiveData<Content> = _content

    fun fetchEpisodeMediaUrl() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data =
                    playerRepository.fetchEpisodeMediaUrl(
                        Constants.getHeader(),
                        "url"
                    ).string()

                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun updateEpisodeContent(content: Content) {

        _content.value = content
    }

}

class PlayerViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            return PlayerViewModel(PlayerRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}