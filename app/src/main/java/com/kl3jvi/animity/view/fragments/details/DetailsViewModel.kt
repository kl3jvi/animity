package com.kl3jvi.animity.view.fragments.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.kl3jvi.animity.model.network.ApiHelper
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Resource
import com.kl3jvi.animity.utils.parser.HtmlParser
import kotlinx.coroutines.Dispatchers

class DetailsViewModel(private val detailsRepository: DetailsRepository) : ViewModel() {
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
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}

class DetailsViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            return DetailsViewModel(DetailsRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}