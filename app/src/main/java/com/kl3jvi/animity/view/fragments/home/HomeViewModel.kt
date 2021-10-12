package com.kl3jvi.animity.view.fragments.home

import android.util.Log
import androidx.lifecycle.*
import com.kl3jvi.animity.model.entities.AnimeMetaModel
import com.kl3jvi.animity.model.network.ApiHelper
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Resource
import com.kl3jvi.animity.utils.parser.HtmlParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeViewModel(
    private val homeRepository: HomeRepository
) : ViewModel() {



     fun fetchRecentSubOrDub() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val response =
                parseList(
                    homeRepository.fetchRecentSubOrDub(
                        Constants.getHeader(),
                        1,
                        Constants.TYPE_RECENT_DUB
                    ).string(), Constants.TYPE_RECENT_DUB
                )

            emit(
                Resource.success(
                    data = response
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

class HomeViewModelFactory(

    private val apiHelper: ApiHelper
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(HomeRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}