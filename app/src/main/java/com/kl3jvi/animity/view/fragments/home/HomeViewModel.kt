package com.kl3jvi.animity.view.fragments.home

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

    fun fetchRecentSubOrDub() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = HtmlParser.parseRecentSubOrDub(
                        homeRepository.fetchRecentSubOrDub(
                            Constants.getHeader(),
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
                    data = parseList(
                        homeRepository.fetchPopularFromAjax(
                            Constants.getHeader(),
                            1
                        ).string(), Constants.TYPE_POPULAR_ANIME
                    )
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun fetchNewSeason() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = parseList(
                        homeRepository.fetchNewSeason(
                            Constants.getHeader(),
                            1
                        ).string(), Constants.TYPE_NEW_SEASON
                    )
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


    fun fetchMovies() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data = parseList(
                        homeRepository.fetchMovies(
                            Constants.getHeader(),
                            1
                        ).string(), Constants.TYPE_MOVIE
                    )
                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun fetchEpisodeMediaUrl() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(
                Resource.success(
                    data =
                    homeRepository.fetchEpisodeMediaUrl(
                        Constants.getHeader(),
                        "/eureka-seven-ao-jungfrau-no-hanabana-tachi-episode-1"
                    ).string()

                )
            )
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


    private fun parseList(response: String, typeValue: Int): ArrayList<AnimeMetaModel> {
        return when (typeValue) {
            Constants.TYPE_RECENT_DUB -> HtmlParser.parseRecentSubOrDub(response, typeValue)
            Constants.TYPE_RECENT_SUB -> HtmlParser.parseRecentSubOrDub(response, typeValue)
            Constants.TYPE_POPULAR_ANIME -> HtmlParser.parsePopular(response, typeValue)
            Constants.TYPE_MOVIE -> HtmlParser.parseMovie(response, typeValue)
            Constants.TYPE_NEW_SEASON -> HtmlParser.parseMovie(response, typeValue)
            else -> ArrayList()
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