package com.kl3jvi.animity.view.fragments.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.kl3jvi.animity.model.entities.AnimeMetaModel
import com.kl3jvi.animity.model.network.ApiHelper
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Resource
import com.kl3jvi.animity.utils.parser.HtmlParser
import kotlinx.coroutines.Dispatchers

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {

    val list = MutableLiveData<ArrayList<AnimeMetaModel>>()


    fun fetchRecentSubOrDub() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = HtmlParser.parseRecentSubOrDub(
                        homeRepository.fetchRecentSubOrDub(
                            mapOf(
                                "referer" to Constants.REFERER,
                                "origin" to Constants.ORIGIN,
                                "user-agent" to Constants.USER_AGENT
                            ),
                            1,
                            Constants.TYPE_RECENT_DUB
                        ).string(), Constants.TYPE_RECENT_DUB
                    )
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


    fun fetchPopularAnime() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = HtmlParser.parsePopular(
                        homeRepository.fetchPopularFromAjax(
                            mapOf(
                                "referer" to Constants.REFERER,
                                "origin" to Constants.ORIGIN,
                                "user-agent" to Constants.USER_AGENT
                            ),
                            1
                        ).string(), Constants.TYPE_RECENT_DUB
                    )
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


}

class HomeViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(HomeRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}