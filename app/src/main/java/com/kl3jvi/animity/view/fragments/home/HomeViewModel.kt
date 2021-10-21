package com.kl3jvi.animity.view.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.liveData
import com.kl3jvi.animity.model.entities.AnimeMetaModel
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Resource
import com.kl3jvi.animity.utils.parser.HtmlParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
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
    }.distinctUntilChanged()

    fun fetchTodaySelectionAnime() = liveData(Dispatchers.IO) {
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
    }.distinctUntilChanged()

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
    }.distinctUntilChanged()

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
    }.distinctUntilChanged()

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

